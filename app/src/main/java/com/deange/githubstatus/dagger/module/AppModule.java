package com.deange.githubstatus.dagger.module;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;


@Module
public class AppModule {

  private final Application mApplication;

  public static AppModule create(final Application application) {
    return new AppModule(application);
  }

  private AppModule(final Application application) {
    mApplication = application;
  }

  @Provides
  public Context providesApplicationContext() {
    return mApplication.getApplicationContext();
  }
}
