package com.deange.githubstatus.net;

import android.util.Log;

import com.deange.githubstatus.MainApplication;
import com.deange.githubstatus.controller.NotificationController;
import com.deange.githubstatus.model.Message;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import javax.inject.Inject;

public class FirebaseService
        extends FirebaseMessagingService {

    private static final String TAG = "FirebaseService";

    @Inject Gson mGson;

    @Override
    public void onCreate() {
        super.onCreate();
        MainApplication.get(this).getAppComponent().inject(this);
    }

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        final String json = mGson.toJson(remoteMessage.getData());
        final Message message = mGson.fromJson(json, Message.class);

        Log.d(TAG, "Message data = " + message);

        NotificationController.getInstance().showNotification(message);
    }

    @Override
    public void onMessageSent(final String msgId) {
    }

    @Override
    public void onSendError(final String msgId, final Exception exception) {
    }

    @Override
    public void onDeletedMessages() {
    }

}
