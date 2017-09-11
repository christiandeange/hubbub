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

    private final Context mContext;
    private final IntentFilter mFilter;
    private Emitter<? super Intent> mEmitter;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            mEmitter.onNext(intent);
        }
    };

    public static Observable<Intent> create(
            final Context context,
            final IntentFilter filter) {

        // Need to create a different observable for each subscriber so that we
        // (un)register our broadcast receiver only once
        return Observable.defer(
                () -> Observable.create(new RxBroadcastReceiver(context, filter)));
    }

    private RxBroadcastReceiver(final Context context, final IntentFilter filter) {
        mContext = context.getApplicationContext();
        mFilter = filter;
    }

    @Override
    public void subscribe(final ObservableEmitter<Intent> emitter) throws Exception {
        mEmitter = emitter;
        mContext.registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void dispose() {
        mContext.unregisterReceiver(mReceiver);
        mReceiver = null;
    }

    @Override
    public boolean isDisposed() {
        return mReceiver == null;
    }
}