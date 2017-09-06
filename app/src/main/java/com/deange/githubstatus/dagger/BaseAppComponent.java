package com.deange.githubstatus.dagger;


import com.deange.githubstatus.MainApplication;
import com.deange.githubstatus.net.FirebaseService;
import com.deange.githubstatus.controller.TopicController;
import com.deange.githubstatus.ui.MainActivity;
import com.deange.githubstatus.ui.PushNotificationDialog;

public interface BaseAppComponent {

    void inject(final MainApplication target);

    void inject(final MainActivity target);

    void inject(final PushNotificationDialog target);

    void inject(final TopicController target);

    void inject(final FirebaseService target);
}
