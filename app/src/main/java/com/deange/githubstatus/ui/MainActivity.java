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
import android.view.View;

import com.deange.githubstatus.BuildConfig;
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
import io.reactivex.subjects.PublishSubject;

import static com.deange.githubstatus.MainApplication.component;
import static com.deange.githubstatus.ui.SpaceDecoration.VERTICAL;
import static com.deange.githubstatus.util.ViewUtils.setVisibility;
import static io.reactivex.Observable.just;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class MainActivity
    extends BaseActivity {

  private static final BarCode BARCODE = BarCode.empty();

  @Inject Store<CurrentStatus, BarCode> mStatusStore;
  @Inject Store<List<Message>, BarCode> mMessageStore;
  @Inject PushNotificationDialog mPushNotificationDialog;
  @Inject DevSettingsDialog mDevSettingsDialog;

  @BindView(R.id.app_bar) AppBarLayout mAppBarLayout;
  @BindView(R.id.toolbar_layout) CollapsingToolbarLayout mToolbarLayout;
  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.fab) FloatingActionButton mFab;
  @BindView(R.id.fab_dev_settings) FloatingActionButton mFabDev;
  @BindView(R.id.swipe_layout) SwipeRefreshLayout mSwipeLayout;
  @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
  @BindView(R.id.empty_view) View mEmptyView;

  @BindDimen(R.dimen.app_bar_elevation) float mElevation;

  private MessagesAdapter mAdapter;
  private final PublishSubject<Boolean> mRefreshing = PublishSubject.create();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    component(this).inject(this);

    setSupportActionBar(mToolbar);

    mAppBarLayout.addOnOffsetChangedListener((layout, off) -> layout.setElevation(mElevation));
    FontUtils.apply(mToolbarLayout, FontUtils.BOLD);
    mSwipeLayout.setOnRefreshListener(this::onRefreshPulled);

    mAdapter = new MessagesAdapter(this);
    mRecyclerView.addItemDecoration(new SpaceDecoration(this, VERTICAL));
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    mRecyclerView.setAdapter(mAdapter);

    setVisibility(mFabDev, BuildConfig.DEBUG);

    mRefreshing.distinctUntilChanged()
               .debounce(refresh -> just(refresh).delay(refresh ? 500L : 0L, MILLISECONDS))
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(mSwipeLayout::setRefreshing);

    mDevSettingsDialog.getUpdateObservable()
                      .compose(bindToLifecycle())
                      .subscribe(a -> refreshStatus());

    refreshStatus();
  }

  @Override
  public int getLayoutId() {
    return R.layout.activity_main;
  }

  @OnClick(R.id.fab)
  public void onFabClicked() {
    mPushNotificationDialog.show(this);
  }

  @OnClick(R.id.fab_dev_settings)
  public void onDevFabClicked() {
    mDevSettingsDialog.show(this);
  }

  public void onRefreshPulled() {
    refreshStatus();
  }

  public void refreshStatus() {
    mRefreshing.onNext(true);

    final Single<Response> response = getResponse().doFinally(() -> mRefreshing.onNext(false));
    response.subscribe(this::onCurrentStatusReceived, this::onCurrentStatusFailed);
    mAdapter.setResponse(response);
  }

  public Single<Response> getResponse() {
    return Single.zip(
        mStatusStore.fetch(BARCODE),
        mMessageStore.fetch(BARCODE),
        Response::create)
                 .onErrorReturn(Response::error)
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .compose(bindToLifecycle());
  }

  void setListVisibility(final boolean isListVisible) {
    setVisibility(mRecyclerView, isListVisible);
    setVisibility(mEmptyView, !isListVisible);
  }

  void onCurrentStatusReceived(final Response response) {
    setListVisibility(!response.messages().isEmpty());
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
    setListVisibility(true);
    error.printStackTrace();
  }
}
