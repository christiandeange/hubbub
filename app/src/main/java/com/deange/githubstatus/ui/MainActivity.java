package com.deange.githubstatus.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.deange.githubstatus.R;
import com.deange.githubstatus.dagger.AppComponent;
import com.deange.githubstatus.ui.common.BaseActivity;
import com.deange.githubstatus.ui.main.MainScreen;
import com.google.android.gms.common.GoogleApiAvailability;

import flow.path.Path;

import static com.deange.githubstatus.ui.scoping.Components.componentInParent;

public class MainActivity
    extends BaseActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    unsubscribeOnDestroy(
        componentInParent(this, AppComponent.class)
            .topicController()
            .getTopic()
            .subscribe(this::onTopicChanged));
  }

  @NonNull
  @Override
  public Path defaultPath() {
    return MainScreen.INSTANCE;
  }

  @Override
  public int layoutId() {
    return R.layout.root;
  }

  private void onTopicChanged(String newTopic) {
    if (!newTopic.isEmpty()) {
      // If the user is attempting to subscribe to a push notification channel,
      // they'll need to have a valid Google Play Services version
      GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this);
    }
  }
}
