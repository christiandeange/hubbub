package com.deange.githubstatus.ui.main;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.deange.githubstatus.BuildConfig;
import com.deange.githubstatus.R;
import com.deange.githubstatus.ui.common.SpaceDecoration;
import com.deange.githubstatus.util.FontUtils;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import static com.deange.githubstatus.ui.scoping.Components.component;
import static com.deange.githubstatus.util.FontUtils.BOLD;
import static com.deange.githubstatus.util.ViewUtils.setVisible;
import static com.deange.githubstatus.util.ViewUtils.unwrap;

public class MainView extends LinearLayout {

  @BindView(R.id.app_bar) AppBarLayout appBarLayout;
  @BindView(R.id.toolbar_layout) CollapsingToolbarLayout toolbarLayout;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.fab_dev_settings) FloatingActionButton devSettings;
  @BindView(R.id.swipe_layout) SwipeRefreshLayout swipeLayout;
  @BindView(R.id.recycler_view) RecyclerView recyclerView;
  @BindView(R.id.empty_view) View emptyView;
  @BindDimen(R.dimen.app_bar_elevation) float elevation;

  @Inject MainPresenter presenter;

  private CompositeDisposable disposables;
  private MessagesAdapter adapter;
  private Bitmap taskIcon;
  private boolean hasLoadedAtLeastOnce;

  public MainView(
      Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);
    component(context, MainScreen.Component.class).inject(this);

    taskIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    ButterKnife.bind(this);

    Context context = getContext();

    appBarLayout.addOnOffsetChangedListener((layout, off) -> layout.setElevation(elevation));
    FontUtils.apply(toolbarLayout, BOLD);
    swipeLayout.setOnRefreshListener(presenter::refreshStatus);

    adapter = new MessagesAdapter();
    recyclerView.addItemDecoration(new SpaceDecoration(context));
    recyclerView.setLayoutManager(new LinearLayoutManager(context));
    recyclerView.setAdapter(adapter);

    setVisible(devSettings, BuildConfig.DEBUG);

    disposables = new CompositeDisposable();
    disposables.add(
        presenter.screenData()
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(this::updateView));

    presenter.takeView(this);
  }

  @Override
  protected void onDetachedFromWindow() {
    presenter.dropView();
    disposables.dispose();
    super.onDetachedFromWindow();
  }

  @OnClick(R.id.fab)
  void onFabClicked() {
    presenter.onFabClicked();
  }

  @OnClick(R.id.fab_dev_settings)
  void onDevFabClicked() {
    presenter.onDevFabClicked();
  }

  private void updateView(MainPresenter.ScreenData screenData) {
    setRefreshing(screenData.refreshing);
    if (screenData.refreshing && hasLoadedAtLeastOnce) {
      // If this is not our first time binding, then keep the existing data on screen
      return;
    }

    hasLoadedAtLeastOnce = true;
    toolbarLayout.setTitle(screenData.title);
    adapter.setMessages(screenData.messages);

    boolean hasMessages = !screenData.messages.isEmpty() || screenData.refreshing;
    setVisible(recyclerView, hasMessages);
    setVisible(emptyView, !hasMessages);

    updateColor(screenData.color);
  }

  public void setRefreshing(boolean refreshing) {
    swipeLayout.setRefreshing(refreshing);
  }

  private void updateColor(@ColorInt int color) {
    toolbarLayout.setBackgroundColor(color);
    toolbarLayout.setContentScrimColor(color);
    toolbarLayout.setStatusBarScrimColor(color);

    Activity activity = unwrap(getContext());
    activity.getWindow().setStatusBarColor(color);
    activity.setTaskDescription(new ActivityManager.TaskDescription(
        getResources().getString(R.string.app_name), taskIcon, color));
  }
}
