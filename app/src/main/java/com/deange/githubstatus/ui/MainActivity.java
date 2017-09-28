package com.deange.githubstatus.ui;

import android.app.ActivityManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.deange.githubstatus.MainApplication;
import com.deange.githubstatus.R;
import com.deange.githubstatus.model.CurrentStatus;
import com.deange.githubstatus.model.Message;
import com.deange.githubstatus.model.Response;
import com.deange.githubstatus.model.State;
import com.deange.githubstatus.util.FontUtils;
import com.nytimes.android.external.store3.base.impl.BarCode;
import com.nytimes.android.external.store3.base.impl.Store;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.deange.githubstatus.ui.SpaceDecoration.VERTICAL;

public class MainActivity
        extends BaseActivity {

    private static final BarCode BARCODE = BarCode.empty();

    @Inject Store<CurrentStatus, BarCode> mStatusStore;
    @Inject Store<List<Message>, BarCode> mMessageStore;

    @BindView(R.id.app_bar) AppBarLayout mAppBarLayout;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.fab) FloatingActionButton mFab;
    @BindView(R.id.swipe_layout) SwipeRefreshLayout mSwipeLayout;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

    @BindDimen(R.dimen.app_bar_elevation) float mElevation;

    private MessagesAdapter mAdapter;

    // Multiple references to the same method do NOT share the same instance
    private final Runnable mRefreshRunnable = this::showRefreshing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainApplication.get(this).getAppComponent().inject(this);

        setSupportActionBar(mToolbar);

        mAppBarLayout.addOnOffsetChangedListener((layout, off) -> layout.setElevation(mElevation));
        FontUtils.apply(mToolbarLayout, FontUtils.THIN);
        mSwipeLayout.setOnRefreshListener(this::onRefreshPulled);

        mAdapter = new MessagesAdapter(this);
        mRecyclerView.addItemDecoration(new SpaceDecoration(this, VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        refreshStatus();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        new PushNotificationDialog(this).show();
    }

    public void onRefreshPulled() {
        refreshStatus();
    }

    public void showRefreshing() {
        mSwipeLayout.setRefreshing(true);
    }

    public void clearRefreshing() {
        removeCallbacks(mRefreshRunnable);
        mSwipeLayout.setRefreshing(false);
    }

    public void refreshStatus() {
        postDelayed(mRefreshRunnable, 500L);

        final Single<Response> response = getResponse().doFinally(this::clearRefreshing);

        response.subscribe(this::onCurrentStatusReceived, this::onCurrentStatusFailed);
        mAdapter.setResponseSingle(response);
    }

    public Single<Response> getResponse() {
        return Single.zip(
                mStatusStore.fetch(BARCODE),
                mMessageStore.fetch(BARCODE),
                Response::create)
                     .subscribeOn(Schedulers.io())
                     .observeOn(AndroidSchedulers.mainThread())
                     .compose(bindToLifecycle());
    }

    void onCurrentStatusReceived(final Response response) {
        final State state = response.status().state();
        final int color = getResources().getColor(state.getColorResId());

        mToolbarLayout.setTitle(getString(state.getTitleResId()).toUpperCase());
        mToolbarLayout.setBackgroundColor(color);
        mToolbarLayout.setContentScrimColor(color);
        mToolbarLayout.setStatusBarScrimColor(color);

        getWindow().setStatusBarColor(color);

        setTaskDescription(new ActivityManager.TaskDescription(
                getString(R.string.app_name),
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher),
                color)
        );
    }

    void onCurrentStatusFailed(final Throwable error) {
        error.printStackTrace();
    }
}
