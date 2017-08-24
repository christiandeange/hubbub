package com.deange.githubstatus.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public abstract class BaseActivity
        extends AppCompatActivity {

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int layoutId = getLayoutId();
        if (layoutId != 0) {
            setContentView(layoutId);
            ButterKnife.bind(this);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onDestroy() {
        disposeAll();

        super.onDestroy();
    }

    protected void trackDisposable(final Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    protected void disposeAll() {
        mCompositeDisposable.dispose();
    }

    protected void post(final Runnable runnable) {
        mHandler.post(runnable);
    }

    protected void postDelayed(final Runnable runnable, final long delayMillis) {
        mHandler.postDelayed(runnable, delayMillis);
    }

    @LayoutRes
    public abstract int getLayoutId();
}
