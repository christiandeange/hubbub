package com.deange.githubstatus.ui;

import android.app.ActivityManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
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
import com.deange.githubstatus.model.Response;
import com.deange.githubstatus.model.State;
import com.deange.githubstatus.util.FontUtils;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;

import static com.deange.githubstatus.MainApplication.component;
import static com.deange.githubstatus.ui.SpaceDecoration.VERTICAL;
import static com.deange.githubstatus.util.ViewUtils.setVisibility;
import static io.reactivex.Observable.just;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class MainActivity
    extends BaseActivity {

  @Inject GithubRunner mRunner;
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

    setTitle(null);
    setSupportActionBar(mToolbar);
    updateColor(getResources().getColor(R.color.state_good));

    mAppBarLayout.addOnOffsetChangedListener((layout, off) -> layout.setElevation(mElevation));
    FontUtils.apply(mToolbarLayout, FontUtils.BOLD);
    mSwipeLayout.setOnRefreshListener(this::onRefreshPulled);

    mAdapter = new MessagesAdapter(this);
    mRecyclerView.addItemDecoration(new SpaceDecoration(this, VERTICAL));
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    mRecyclerView.setAdapter(mAdapter);

    setVisibility(mFabDev, BuildConfig.DEBUG);

    unsubscribeOnDestroy(
        mRefreshing.distinctUntilChanged()
                   .debounce(refresh -> just(refresh).delay(refresh ? 1000L : 0L, MILLISECONDS))
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe(mSwipeLayout::setRefreshing));

    unsubscribeOnDestroy(
        mDevSettingsDialog.onDevSettingsChanged()
                          .subscribe(a -> refreshStatus()));

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

    unsubscribeOnDestroy(mAdapter.refreshStatus());
    unsubscribeOnDestroy(mRunner.getStatus()
                                .doFinally(() -> mRefreshing.onNext(false))
                                .subscribe(this::onStatusReceived, this::onStatusFailed));
  }

  void setListVisibility(final boolean isListVisible) {
    setVisibility(mRecyclerView, isListVisible);
    setVisibility(mEmptyView, !isListVisible);
  }

  void onStatusReceived(final Response response) {
    setListVisibility(!response.messages().isEmpty());
    final State state = response.status().state();
    final int color = getResources().getColor(state.getColorResId());

    mToolbarLayout.setTitle(getString(state.getTitleResId()).toUpperCase());
    updateColor(color);
  }

  private void updateColor(@ColorInt final int color) {
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

  void onStatusFailed(final Throwable error) {
    setListVisibility(true);
    error.printStackTrace();
  }

}
