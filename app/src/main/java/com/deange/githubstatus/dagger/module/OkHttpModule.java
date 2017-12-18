package com.deange.githubstatus.dagger.module;

import com.deange.githubstatus.BuildConfig;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


@Module
public class OkHttpModule {

  @Provides
  @Singleton
  public OkHttpClient providesOkHttpClient() {
    final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    loggingInterceptor.setLevel(BuildConfig.DEBUG
                                    ? HttpLoggingInterceptor.Level.BODY
                                    : HttpLoggingInterceptor.Level.NONE);

    return new OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build();
  }

}
