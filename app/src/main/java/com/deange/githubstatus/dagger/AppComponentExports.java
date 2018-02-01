package com.deange.githubstatus.dagger;

import com.deange.githubstatus.controller.GithubController;
import com.deange.githubstatus.controller.NotificationController;
import com.deange.githubstatus.controller.TopicController;
import com.deange.githubstatus.ui.main.MainScreen;
import com.google.gson.Gson;

@SuppressWarnings("unused")
interface AppComponentExports {

  MainScreen.Component mainComponent();

  TopicController topicController();

  NotificationController notificationController();

  GithubController githubController();

  Gson gson();
}
