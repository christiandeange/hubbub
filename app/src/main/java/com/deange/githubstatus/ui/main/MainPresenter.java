package com.deange.githubstatus.ui.main;

import android.content.res.Resources;
import android.support.annotation.ColorInt;

import com.deange.githubstatus.R;
import com.deange.githubstatus.controller.GithubController;
import com.deange.githubstatus.model.Message;
import com.deange.githubstatus.model.Response;
import com.deange.githubstatus.model.State;
import com.deange.githubstatus.ui.DevSettingsDialog;
import com.deange.githubstatus.ui.PushNotificationDialog;
import com.deange.githubstatus.ui.common.Presenter;
import com.deange.githubstatus.ui.scoping.SingleIn;
import com.deange.githubstatus.util.Unit;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

import static com.deange.githubstatus.util.RxUtils.resubscribeWhen;
import static java.util.Collections.emptyList;

@SingleIn(MainScreen.class)
public class MainPresenter extends Presenter<MainView> {

  private final Resources res;
  private final GithubController runner;
  private final PushNotificationDialog pushNotificationDialog;
  private final DevSettingsDialog devSettingsDialog;

  private final BehaviorSubject<Unit> refresh = BehaviorSubject.create();
  private final Observable<ScreenData> screenData;

  @Inject
  MainPresenter(
      Resources res,
      GithubController runner,
      PushNotificationDialog pushNotificationDialog,
      DevSettingsDialog devSettingsDialog) {
    this.res = res;
    this.runner = runner;
    this.pushNotificationDialog = pushNotificationDialog;
    this.devSettingsDialog = devSettingsDialog;

    screenData = this.runner.getStatus()
                   .toObservable()
                   .map(this::screenDataForResponse)
                   .onErrorReturn(this::screenDataForError)
                   .subscribeOn(Schedulers.io())
                   .compose(resubscribeWhen(refresh))
                   .cache();

    refresh.onNext(Unit.INSTANCE);
  }

  @Override
  protected void onLoad(MainView view) {
    unsubscribeOnUnload(
        devSettingsDialog.onDevSettingsChanged()
                         .subscribe(a -> refreshStatus()));
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
    refresh.onNext(Unit.INSTANCE);
  }

  private ScreenData screenDataForResponse(Response response) {
    return createScreenData(response.status().state(), response.messages());
  }

  private ScreenData screenDataForLoading() {
    String title = res.getString(R.string.loading);
    int color = res.getColor(R.color.state_error);
    return new ScreenData(true, title, color, emptyList());
  }

  private ScreenData screenDataForError(Throwable throwable) {
    throwable.printStackTrace();
    return createScreenData(State.ERROR, emptyList());
  }

  private ScreenData createScreenData(State state, List<Message> messages) {
    String title = res.getString(state.getStateResId()).toUpperCase();
    int color = res.getColor(state.getColorResId());

    return new ScreenData(false, title, color, messages);
  }

  public static class ScreenData {
    public final boolean refreshing;
    public final CharSequence title;
    public final @ColorInt int color;
    public final List<Message> messages;

    public ScreenData(
        boolean refreshing,
        CharSequence title,
        @ColorInt int color,
        List<Message> messages) {
      this.refreshing = refreshing;
      this.title = title;
      this.color = color;
      this.messages = messages;
    }
  }
}
