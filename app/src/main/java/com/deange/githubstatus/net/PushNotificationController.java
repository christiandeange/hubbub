package com.deange.githubstatus.net;

import android.content.Context;

import com.deange.githubstatus.MainApplication;
import com.deange.githubstatus.util.RxPreference;
import com.f2prateek.rx.preferences2.Preference;

import javax.inject.Inject;

public class PushNotificationController {

    private static PushNotificationController sInstance;

    @Inject @RxPreference("topic") Preference<String> mTopicPreference;
    private String mTopic;

    public static synchronized void createInstance(final Context context) {
        if (sInstance != null) {
            throw new IllegalStateException("PushNotificationController was already initialized");
        }
        sInstance = new PushNotificationController(context);
    }

    public static synchronized PushNotificationController getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("PushNotificationController has not been initialized");
        }
        return sInstance;
    }

    public PushNotificationController(final Context context) {
        MainApplication.get(context).getAppComponent().inject(this);

        mTopicPreference.asObservable().subscribe(this::onTopicSubscriptionChanged);
    }

    private void onTopicSubscriptionChanged(final String newTopic) {
        final String oldTopic = mTopic;
        mTopic = newTopic;

        if (oldTopic != null) {
            // TODO unsubscribe from existing topic
        }

        // TODO subscribe to new topic
    }

}
