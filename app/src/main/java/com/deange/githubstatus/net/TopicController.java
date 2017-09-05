package com.deange.githubstatus.net;

import android.content.Context;
import android.util.Log;

import com.deange.githubstatus.MainApplication;
import com.deange.githubstatus.model.TopicChange;
import com.deange.githubstatus.util.RxPreference;
import com.f2prateek.rx.preferences2.Preference;
import com.google.firebase.messaging.FirebaseMessaging;

import javax.inject.Inject;

import io.reactivex.Observable;

public class TopicController {

    private static final String TAG = "TopicController";
    private static TopicController sInstance;

    @Inject @RxPreference("topic") Preference<String> mTopicPreference;

    private final FirebaseMessaging mFirebase;

    public static synchronized void createInstance(final Context context) {
        if (sInstance != null) {
            throw new IllegalStateException("TopicController was already initialized");
        }
        sInstance = new TopicController(context);
    }

    public static synchronized TopicController getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("TopicController has not been initialized");
        }
        return sInstance;
    }

    public TopicController(final Context context) {
        MainApplication.get(context).getAppComponent().inject(this);

        mFirebase = FirebaseMessaging.getInstance();
        getTopicChanges().subscribe(this::onTopicSubscriptionChanged);
    }

    public Observable<String> getTopic() {
        return mTopicPreference.asObservable();
    }

    public Observable<TopicChange> getTopicChanges() {
        return mTopicPreference.asObservable()
                               .scan(TopicChange.create(), TopicChange::pushTopic)
                               .skip(2); // Skips initial seed and first automatic emission
    }

    private void onTopicSubscriptionChanged(final TopicChange change) {
        Log.d(TAG, "Topic changed --> " + change);

        if (!change.oldTopic().isEmpty()) {
            mFirebase.unsubscribeFromTopic(change.oldTopic());
        }

        if (!change.newTopic().isEmpty()) {
            mFirebase.subscribeToTopic(change.newTopic());
        }
    }

}
