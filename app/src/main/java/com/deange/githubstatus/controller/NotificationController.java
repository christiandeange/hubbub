package com.deange.githubstatus.controller;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.deange.githubstatus.R;
import com.deange.githubstatus.model.Message;
import com.deange.githubstatus.model.State;
import com.deange.githubstatus.net.FirebaseService;
import com.deange.githubstatus.ui.MainActivity;
import com.deange.githubstatus.util.RxBroadcastReceiver;

import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import mortar.MortarScope;
import mortar.Scoped;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.deange.githubstatus.net.FirebaseService.ACTION_MESSAGE_RECEIVED;

public class NotificationController implements Scoped {

  private static final String TAG = "NotificationController";
  private static final int NOTIFICATION_ID = 0x420;

  private final Context context;
  private final NotificationManager manager;
  private Disposable subscription;

  @Inject
  public NotificationController(Context context) {
    this.context = context;
    manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
  }

  @Override
  public void onEnterScope(MortarScope scope) {
    ensureNotificationChannels();

    // Listen for incoming status message updates
    subscription = RxBroadcastReceiver.create(context, new IntentFilter(ACTION_MESSAGE_RECEIVED))
                                      .map(FirebaseService::getMessageFromIntent)
                                      .subscribe(this::showNotification);
  }

  @Override
  public void onExitScope() {
    subscription.dispose();
    subscription = null;
  }

  public void showNotification(Message message) {
    State state = message.state();

    String channelId = state.name();

    String title = context.getString(R.string.app_name);
    String description = message.bodyForNotification(context);
    int icon = R.drawable.ic_notification_git;
    int color = ContextCompat.getColor(context, state.getColorResId());

    NotificationCompat.Builder builder =
        new NotificationCompat.Builder(context, channelId);
    builder.setContentTitle(title);
    builder.setContentText(description);
    builder.setSmallIcon(icon);
    builder.setColor(color);
    builder.setStyle(new NotificationCompat.BigTextStyle());

    builder.setAutoCancel(true);
    builder.setContentIntent(PendingIntent.getActivity(
        context,
        NOTIFICATION_ID,
        new Intent(context, MainActivity.class),
        PendingIntent.FLAG_UPDATE_CURRENT));

    manager.notify(NOTIFICATION_ID, builder.build());
  }

  private void ensureNotificationChannels() {
    if (!supportsNotificationChannels()) {
      return;
    }

    Map<String, NotificationChannel> existingChannels =
        manager.getNotificationChannels()
               .stream()
               .collect(Collectors.toMap(
                   NotificationChannel::getId,
                   channel -> channel));

    for (State state : State.values()) {
      String id = state.name();
      if (existingChannels.containsKey(id)) {
        existingChannels.remove(id);
        continue;
      }

      String name = context.getString(state.getTitleResId());
      String description = context.getString(state.getDescriptionResId());
      int color = ContextCompat.getColor(context, state.getColorResId());
      int importance = NotificationManager.IMPORTANCE_DEFAULT;

      NotificationChannel channel = new NotificationChannel(id, name, importance);
      channel.setDescription(description);
      channel.enableVibration(true);
      channel.enableLights(true);
      channel.setLightColor(color);

      Log.d(TAG, "Creating notification channel: " + id);
      manager.createNotificationChannel(channel);
    }

    // Leftover channels (maybe from an update?)
    for (String staleChannelId : existingChannels.keySet()) {
      Log.d(TAG, "Deleting notification channel: " + staleChannelId);
      manager.deleteNotificationChannel(staleChannelId);
    }

    existingChannels.clear();
  }

  private boolean supportsNotificationChannels() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
  }
}
