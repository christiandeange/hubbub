package com.deange.githubstatus.net;

import android.content.Intent;
import android.util.Log;

import com.deange.githubstatus.model.Message;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Map;

import javax.inject.Inject;

import static com.deange.githubstatus.MainApplication.component;

public class FirebaseService
    extends FirebaseMessagingService {

  private static final String TAG = "FirebaseService";
  private static final String KEY_MESSAGE = "message";

  public static final String ACTION_MESSAGE_RECEIVED = "hubbub.intent.action.message_received";

  @Inject Gson gson;

  @Override
  public void onCreate() {
    super.onCreate();
    component(this).inject(this);
  }

  @Override
  public void onMessageReceived(final RemoteMessage remoteMessage) {
    final JsonObject jsonObject = new JsonObject();
    for (final Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
      jsonObject.addProperty(entry.getKey(), entry.getValue());
    }

    final Message message = gson.fromJson(jsonObject, Message.class);

    Log.d(TAG, "Message data = " + message);

    final Intent intent = new Intent(ACTION_MESSAGE_RECEIVED);
    intent.putExtra(KEY_MESSAGE, message);
    sendBroadcast(intent);
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

  public static Message getMessageFromIntent(final Intent intent) {
    return intent.getParcelableExtra(KEY_MESSAGE);
  }

}
