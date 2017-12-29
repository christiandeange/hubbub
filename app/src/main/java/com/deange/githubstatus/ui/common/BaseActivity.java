package com.deange.githubstatus.ui.common;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.GoogleApiAvailability;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.deange.githubstatus.MainApplication.component;


public abstract class BaseActivity
    extends AppCompatActivity {

  private final CompositeDisposable disposables = new CompositeDisposable();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    unsubscribeOnDestroy(
        component(this).topicController()
                       .getTopic()
                       .subscribe(this::onTopicChanged));
  }

  private void onTopicChanged(final String newTopic) {
    if (!newTopic.isEmpty()) {
      // If the user is attempting to subscribe to a push notification channel,
      // they'll need to have a valid Google Play Services version
      GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this);
    }
  }

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  @Override
  protected void onDestroy() {
    disposables.dispose();
    super.onDestroy();
  }

  public void unsubscribeOnDestroy(final Disposable disposable) {
    disposables.add(disposable);
  }
}
