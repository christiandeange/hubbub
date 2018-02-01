package com.deange.githubstatus.net;

import android.content.Intent;
import android.util.Log;

import com.deange.githubstatus.dagger.AppComponent;
import com.deange.githubstatus.model.Message;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Map;

import static com.deange.githubstatus.ui.scoping.Components.componentInParent;

public class FirebaseService
    extends FirebaseMessagingService {

  private static final String TAG = "FirebaseService";
  private static final String KEY_MESSAGE = "message";

  public static final String ACTION_MESSAGE_RECEIVED = "hubbub.intent.action.message_received";

  private Gson gson;

  @Override
  public void onCreate() {
    super.onCreate();
  }

  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    if (gson == null) {
      gson = componentInParent(this, AppComponent.class).gson();
    }

    JsonObject jsonObject = new JsonObject();
    for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
      jsonObject.addProperty(entry.getKey(), entry.getValue());
    }

    Message message = gson.fromJson(jsonObject, Message.class);

    Log.d(TAG, "Message data = " + message);

    Intent intent = new Intent(ACTION_MESSAGE_RECEIVED);
    intent.putExtra(KEY_MESSAGE, message);
    sendBroadcast(intent);
  }

  @Override
  public void onMessageSent(String msgId) {
  }

  @Override
  public void onSendError(String msgId, Exception exception) {
  }

  @Override
  public void onDeletedMessages() {
  }

  public static Message getMessageFromIntent(Intent intent) {
    return intent.getParcelableExtra(KEY_MESSAGE);
  }

}
