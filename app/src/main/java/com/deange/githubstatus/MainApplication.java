package com.deange.githubstatus;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.deange.githubstatus.controller.NotificationController;
import com.deange.githubstatus.dagger.BaseAppComponent;
import com.deange.githubstatus.dagger.DaggerAppComponent;
import com.deange.githubstatus.dagger.module.AppModule;
import com.deange.githubstatus.controller.TopicController;
import com.deange.githubstatus.util.FontUtils;

import net.danlew.android.joda.JodaTimeAndroid;


public class MainApplication
        extends Application {

    private static final String TAG = "MainApplication";

    private BaseAppComponent mAppComponent;

    public static MainApplication get(final Context context) {
        return (MainApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate()");
        super.onCreate();

        mAppComponent = buildAppComponent();
        mAppComponent.inject(this);

        FontUtils.init(this);

        JodaTimeAndroid.init(this);

        TopicController.createInstance(this);

        NotificationController.createInstance(this);
    }

    private BaseAppComponent buildAppComponent() {
        return DaggerAppComponent.builder()
                .appModule(AppModule.create(this))
                .build();
    }

    public <T extends BaseAppComponent> T getAppComponent() {
        // noinspection unchecked
        return (T) mAppComponent;
    }

}
