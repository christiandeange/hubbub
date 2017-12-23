package com.deange.githubstatus.ui;

import android.app.ActivityManager;
import android.graphics.BitmapFactory;
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

  @Inject GithubRunner runner;
  @Inject PushNotificationDialog pushNotificationDialog;
  @Inject DevSettingsDialog devSettingsDialog;

  @BindView(R.id.app_bar) AppBarLayout appBarLayout;
  @BindView(R.id.toolbar_layout) CollapsingToolbarLayout toolbarLayout;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.fab) FloatingActionButton fab;
  @BindView(R.id.fab_dev_settings) FloatingActionButton fabDev;
  @BindView(R.id.swipe_layout) SwipeRefreshLayout swipeLayout;
  @BindView(R.id.recycler_view) RecyclerView recyclerView;
  @BindView(R.id.empty_view) View emptyView;

  @BindDimen(R.dimen.app_bar_elevation) float elevation;

  private MessagesAdapter adapter;
  private final PublishSubject<Boolean> refreshing = PublishSubject.create();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    component(this).inject(this);

    setTitle(null);
    setSupportActionBar(toolbar);
    updateColor(getResources().getColor(R.color.state_good));

    appBarLayout.addOnOffsetChangedListener((layout, off) -> layout.setElevation(elevation));
    FontUtils.apply(toolbarLayout, FontUtils.BOLD);
    swipeLayout.setOnRefreshListener(this::onRefreshPulled);

    adapter = new MessagesAdapter(this);
    recyclerView.addItemDecoration(new SpaceDecoration(this, VERTICAL));
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(adapter);

    setVisibility(fabDev, BuildConfig.DEBUG);

    unsubscribeOnDestroy(
        refreshing.distinctUntilChanged()
                  .debounce(refresh -> just(refresh).delay(refresh ? 1000L : 0L, MILLISECONDS))
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(swipeLayout::setRefreshing));

    unsubscribeOnDestroy(
        devSettingsDialog.onDevSettingsChanged()
                         .subscribe(a -> refreshStatus()));

    refreshStatus();
  }

  @Override
  public int getLayoutId() {
    return R.layout.activity_main;
  }

  @OnClick(R.id.fab)
  public void onFabClicked() {
    pushNotificationDialog.show(this);
  }

  @OnClick(R.id.fab_dev_settings)
  public void onDevFabClicked() {
    devSettingsDialog.show(this);
  }

  public void onRefreshPulled() {
    refreshStatus();
  }

  public void refreshStatus() {
    refreshing.onNext(true);

    unsubscribeOnDestroy(adapter.refreshStatus());
    unsubscribeOnDestroy(runner.getStatus()
                               .doFinally(() -> refreshing.onNext(false))
                               .subscribe(this::onStatusReceived, this::onStatusFailed));
  }

  void setListVisibility(final boolean isListVisible) {
    setVisibility(recyclerView, isListVisible);
    setVisibility(emptyView, !isListVisible);
  }

  void onStatusReceived(final Response response) {
    setListVisibility(!response.messages().isEmpty());
    final State state = response.status().state();
    final int color = getResources().getColor(state.getColorResId());

    toolbarLayout.setTitle(getString(state.getTitleResId()).toUpperCase());
    updateColor(color);
  }

  private void updateColor(@ColorInt final int color) {
    toolbarLayout.setBackgroundColor(color);
    toolbarLayout.setContentScrimColor(color);
    toolbarLayout.setStatusBarScrimColor(color);
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
