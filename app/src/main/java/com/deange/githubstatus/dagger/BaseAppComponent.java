package com.deange.githubstatus.dagger;


import com.deange.githubstatus.controller.NotificationController;
import com.deange.githubstatus.controller.TopicController;
import com.deange.githubstatus.net.FirebaseService;
import com.deange.githubstatus.ui.MainActivity;
import com.deange.githubstatus.ui.MainView;

public interface BaseAppComponent {

  TopicController topicController();

  NotificationController notificationController();

  // Injection sites
  void inject(MainActivity target);
  void inject(MainView target);
  void inject(FirebaseService target);
}
