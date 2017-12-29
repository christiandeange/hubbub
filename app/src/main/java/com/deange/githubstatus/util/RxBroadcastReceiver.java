package com.deange.githubstatus.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;

public class RxBroadcastReceiver
    implements
    ObservableOnSubscribe<Intent>,
    Disposable {

  private final Context context;
  private final IntentFilter filter;
  private Emitter<? super Intent> emitter;

  private BroadcastReceiver receiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      emitter.onNext(intent);
    }
  };

  public static Observable<Intent> create(Context context, IntentFilter filter) {

    // Need to create a different observable for each subscriber so that we
    // (un)register our broadcast receiver only once
    return Observable.defer(
        () -> Observable.create(new RxBroadcastReceiver(context, filter)));
  }

  private RxBroadcastReceiver(Context context, IntentFilter filter) {
    this.context = context.getApplicationContext();
    this.filter = filter;
  }

  @Override
  public void subscribe(ObservableEmitter<Intent> emitter) throws Exception {
    this.emitter = emitter;
    context.registerReceiver(receiver, filter);
  }

  @Override
  public void dispose() {
    context.unregisterReceiver(receiver);
    receiver = null;
  }

  @Override
  public boolean isDisposed() {
    return receiver == null;
  }
}