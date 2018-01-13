package com.deange.githubstatus.ui;

import android.content.Context;
import android.content.res.Resources;
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
import com.deange.githubstatus.controller.GithubController;
import com.deange.githubstatus.model.Response;
import com.deange.githubstatus.model.State;
import com.deange.githubstatus.ui.common.Presenter;
import com.deange.githubstatus.ui.common.SpaceDecoration;
import com.deange.githubstatus.util.FontUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;

import static com.deange.githubstatus.ui.common.SpaceDecoration.VERTICAL;
import static com.deange.githubstatus.util.FontUtils.BOLD;
import static com.deange.githubstatus.util.ViewUtils.setVisibility;
import static io.reactivex.Observable.just;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Singleton
public class MainPresenter extends Presenter<View> {

  @BindView(R.id.app_bar) AppBarLayout appBarLayout;
  @BindView(R.id.toolbar_layout) CollapsingToolbarLayout toolbarLayout;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.fab_dev_settings) FloatingActionButton devSettings;
  @BindView(R.id.swipe_layout) SwipeRefreshLayout swipeLayout;
  @BindView(R.id.recycler_view) RecyclerView recyclerView;
  @BindView(R.id.empty_view) View emptyView;
  @BindDimen(R.dimen.app_bar_elevation) float elevation;

  private GithubController runner;
  private PushNotificationDialog pushNotificationDialog;
  private DevSettingsDialog devSettingsDialog;

  private MessagesAdapter adapter;
  private final PublishSubject<Boolean> refreshing = PublishSubject.create();
  private final PublishSubject<Integer> colorUpdates = PublishSubject.create();

  @Inject
  MainPresenter(
      GithubController runner,
      PushNotificationDialog pushNotificationDialog,
      DevSettingsDialog devSettingsDialog) {
    this.runner = runner;
    this.pushNotificationDialog = pushNotificationDialog;
    this.devSettingsDialog = devSettingsDialog;
  }

  @Override
  protected void onLoad(View view) {
    Context context = view.getContext();

    appBarLayout.addOnOffsetChangedListener((layout, off) -> layout.setElevation(elevation));
    FontUtils.apply(toolbarLayout, BOLD);
    swipeLayout.setOnRefreshListener(this::refreshStatus);

    adapter = new MessagesAdapter(context);
    recyclerView.addItemDecoration(new SpaceDecoration(context, VERTICAL));
    recyclerView.setLayoutManager(new LinearLayoutManager(context));
    recyclerView.setAdapter(adapter);

    setVisibility(devSettings, BuildConfig.DEBUG);

    unsubscribeOnUnload(
        refreshing.distinctUntilChanged()
                  .debounce(refresh -> just(refresh).delay(refresh ? 1000L : 0L, MILLISECONDS))
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(swipeLayout::setRefreshing));

    unsubscribeOnUnload(
        devSettingsDialog.onDevSettingsChanged()
                         .subscribe(a -> refreshStatus()));

    updateColor(context.getResources().getColor(R.color.state_good));
    refreshStatus();
  }

  public Toolbar toolbar() {
    return toolbar;
  }

  public Observable<Integer> onColorUpdated() {
    return colorUpdates;
  }

  @OnClick(R.id.fab)
  void onFabClicked() {
    pushNotificationDialog.show(getView().getContext());
  }

  @OnClick(R.id.fab_dev_settings)
  void onDevFabClicked() {
    devSettingsDialog.show(getView().getContext());
  }

  private void refreshStatus() {
    refreshing.onNext(true);

    unsubscribeOnUnload(adapter.refreshStatus());
    unsubscribeOnUnload(runner.getStatus()
                              .doFinally(() -> refreshing.onNext(false))
                              .subscribe(this::onStatusReceived, this::onStatusFailed));
  }

  private void onStatusReceived(Response response) {
    Resources res = getView().getResources();
    setListVisibility(!response.messages().isEmpty());
    State state = response.status().state();
    int color = res.getColor(state.getColorResId());

    toolbarLayout.setTitle(res.getString(state.getStateResId()).toUpperCase());
    updateColor(color);
  }

  private void onStatusFailed(Throwable error) {
    setListVisibility(true);
    error.printStackTrace();
  }

  private void setListVisibility(boolean isListVisible) {
    setVisibility(recyclerView, isListVisible);
    setVisibility(emptyView, !isListVisible);
  }

  private void updateColor(@ColorInt int color) {
    toolbarLayout.setBackgroundColor(color);
    toolbarLayout.setContentScrimColor(color);
    toolbarLayout.setStatusBarScrimColor(color);
    colorUpdates.onNext(color);
  }
}
