package com.deange.githubstatus.dagger;


import com.deange.githubstatus.MainApplication;
import com.deange.githubstatus.net.PushNotificationController;
import com.deange.githubstatus.ui.MainActivity;
import com.deange.githubstatus.ui.PushNotificationDialog;

public interface BaseAppComponent {

    void inject(final MainApplication target);

    void inject(final MainActivity target);

    void inject(final PushNotificationDialog target);

    void inject(final PushNotificationController target);
}
