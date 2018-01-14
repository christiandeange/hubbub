package com.deange.githubstatus.dagger;


import com.deange.githubstatus.MainApplication;
import com.deange.githubstatus.controller.NotificationController;
import com.deange.githubstatus.controller.TopicController;
import com.deange.githubstatus.net.FirebaseService;
import com.deange.githubstatus.ui.DevSettingsDialog;
import com.deange.githubstatus.ui.MainActivity;
import com.deange.githubstatus.ui.MessagesAdapter;

public interface BaseAppComponent {

  TopicController topicController();

  NotificationController notificationController();

  // Injection sites
  void inject(MainApplication target);
  void inject(MainActivity target);
  void inject(FirebaseService target);
  void inject(MessagesAdapter target);
}
