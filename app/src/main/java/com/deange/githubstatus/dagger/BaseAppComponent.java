package com.deange.githubstatus.dagger;


import com.deange.githubstatus.MainApplication;
import com.deange.githubstatus.controller.NotificationController;
import com.deange.githubstatus.controller.TopicController;
import com.deange.githubstatus.net.FirebaseService;
import com.deange.githubstatus.ui.MainActivity;

public interface BaseAppComponent {

    TopicController topicController();

    NotificationController notificationController();

    // Injection sites
    void inject(final MainApplication target);
    void inject(final MainActivity target);
    void inject(final FirebaseService target);
}
