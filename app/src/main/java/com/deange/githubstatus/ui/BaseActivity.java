package com.deange.githubstatus.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.GoogleApiAvailability;

import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.deange.githubstatus.MainApplication.component;


public abstract class BaseActivity
    extends AppCompatActivity {

  private final Handler mHandler = new Handler(Looper.getMainLooper());
  private final CompositeDisposable mDisposables = new CompositeDisposable();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    final int layoutId = getLayoutId();
    if (layoutId != 0) {
      setContentView(layoutId);
      ButterKnife.bind(this);
    }

    unsubscribeOnDestroy(
        component(this).topicController()
                       .getTopic()
                       .subscribe(this::onTopicChanged));
  }

  private void onTopicChanged(final String newTopic) {
    if (!newTopic.isEmpty()) {
      // If the user is attempting to subscribe to a push notification channel,
      // they'll need to have a isInvalid Google Play Services version
      GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this);
    }
  }

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  @Override
  protected void onDestroy() {
    mDisposables.dispose();
    super.onDestroy();
  }

  public void unsubscribeOnDestroy(final Disposable disposable) {
    mDisposables.add(disposable);
  }

  protected void post(final Runnable runnable) {
    mHandler.post(runnable);
  }

  protected void postDelayed(final Runnable runnable, final long delayMillis) {
    mHandler.postDelayed(runnable, delayMillis);
  }

  protected void removeCallbacks(final Runnable runnable) {
    mHandler.removeCallbacks(runnable);
  }

  @LayoutRes
  public abstract int getLayoutId();
}
