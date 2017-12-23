package com.deange.githubstatus.controller;

import android.util.Log;

import com.deange.githubstatus.model.TopicChange;
import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.google.firebase.messaging.FirebaseMessaging;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class TopicController {

  private static final String TAG = "TopicController";

  private final FirebaseMessaging firebase;
  private final Preference<String> topicPreference;

  @Inject
  public TopicController(final RxSharedPreferences preferences) {
    firebase = FirebaseMessaging.getInstance();
    topicPreference = preferences.getString("topic");
    getTopicChanges().subscribe(this::onTopicSubscriptionChanged);
  }

  public Preference<String> getPreference() {
    return topicPreference;
  }

  public Observable<String> getTopic() {
    return topicPreference.asObservable();
  }

  public Observable<TopicChange> getTopicChanges() {
    return topicPreference.asObservable()
                          .scan(TopicChange.create(), TopicChange::pushTopic)
                          .skip(2); // Skips initial seed and first automatic emission
  }

  private void onTopicSubscriptionChanged(final TopicChange change) {
    Log.d(TAG, "Topic changed --> " + change);

    if (!change.oldTopic().isEmpty()) {
      firebase.unsubscribeFromTopic(change.oldTopic());
    }

    if (!change.newTopic().isEmpty()) {
      firebase.subscribeToTopic(change.newTopic());
    }
  }
}
