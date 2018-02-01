package com.deange.githubstatus;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.deange.githubstatus.dagger.AppComponent;
import com.deange.githubstatus.dagger.AppModule;
import com.deange.githubstatus.dagger.AppScope;
import com.deange.githubstatus.dagger.DaggerAppComponent;
import com.deange.githubstatus.util.FontUtils;

import net.danlew.android.joda.JodaTimeAndroid;

import mortar.MortarScope;

import static com.deange.githubstatus.ui.scoping.MortarContextFactory.createRootScope;


public class MainApplication
    extends Application {

  private static final String TAG = "MainApplication";

  private AppComponent appComponent;
  private MortarScope rootScope;

  public static MainApplication get(Context context) {
    return (MainApplication) context.getApplicationContext();
  }

  public MainApplication() {
    // Must be done in the constructor since FirebaseService.onCreate()
    // is instantiated before the application goes through onCreate()
    appComponent = DaggerAppComponent
        .builder()
        .appModule(AppModule.create(this))
        .build();
    rootScope = createRootScope(appComponent, AppScope.class.getSimpleName());
  }

  @Override
  public void onCreate() {
    Log.d(TAG, "onCreate()");
    super.onCreate();

    rootScope.register(appComponent.notificationController());

    FontUtils.init(this);
    JodaTimeAndroid.init(this);
  }

  @Override
  public Object getSystemService(String name) {
    return rootScope.hasService(name)
        ? rootScope.getService(name)
        : super.getSystemService(name);
  }

}
