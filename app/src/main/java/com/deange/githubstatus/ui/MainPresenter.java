package com.deange.githubstatus.ui;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.Toolbar;

import com.deange.githubstatus.R;
import com.deange.githubstatus.controller.GithubController;
import com.deange.githubstatus.model.Message;
import com.deange.githubstatus.model.Response;
import com.deange.githubstatus.model.State;
import com.deange.githubstatus.ui.common.Presenter;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;

import static io.reactivex.Observable.just;
import static java.util.Collections.emptyList;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Singleton
public class MainPresenter extends Presenter<MainView> {

  private GithubController runner;
  private PushNotificationDialog pushNotificationDialog;
  private DevSettingsDialog devSettingsDialog;

  private final PublishSubject<Boolean> refreshing = PublishSubject.create();
  private final PublishSubject<Integer> colorUpdates = PublishSubject.create();
  private final PublishSubject<ScreenData> screenData = PublishSubject.create();

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
  protected void onLoad(MainView view) {
    Context context = view.getContext();

    unsubscribeOnUnload(
        refreshing.distinctUntilChanged()
                  .debounce(refresh -> just(refresh).delay(refresh ? 1000L : 0L, MILLISECONDS))
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(view::setRefreshing));

    unsubscribeOnUnload(
        devSettingsDialog.onDevSettingsChanged()
                         .subscribe(a -> refreshStatus()));

    colorUpdates.onNext(context.getResources().getColor(R.color.state_good));
  }

  public Toolbar toolbar() {
    return getView().toolbar;
  }

  public Observable<Integer> onColorUpdated() {
    return colorUpdates;
  }

  public Observable<Boolean> refreshing() {
    return refreshing;
  }

  public Observable<ScreenData> screenData() {
    return screenData;
  }

  void onFabClicked() {
    pushNotificationDialog.show(getView().getContext());
  }

  void onDevFabClicked() {
    devSettingsDialog.show(getView().getContext());
  }

  void refreshStatus() {
    refreshing.onNext(true);

    unsubscribeOnUnload(runner.getStatus()
                              .doFinally(() -> refreshing.onNext(false))
                              .subscribe(this::onStatusReceived, this::onStatusFailed));
  }

  private void onStatusReceived(Response response) {
    updateState(response.status().state(), response.messages());
  }

  private void onStatusFailed(Throwable error) {
    error.printStackTrace();
    updateState(State.ERROR, emptyList());
  }

  private void updateState(State state, List<Message> messages) {
    Resources res = getView().getResources();
    String title = res.getString(state.getStateResId()).toUpperCase();
    int color = res.getColor(state.getColorResId());

    screenData.onNext(new ScreenData(title, messages));
    colorUpdates.onNext(color);
  }

  public static class ScreenData {
    public final CharSequence title;
    public final List<Message> messages;

    public ScreenData(CharSequence title, List<Message> messages) {
      this.title = title;
      this.messages = messages;
    }
  }
}
