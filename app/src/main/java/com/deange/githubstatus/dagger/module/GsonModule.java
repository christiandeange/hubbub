package com.deange.githubstatus.dagger.module;

import com.deange.githubstatus.converter.LocalDateTimeConverterFactory;
import com.deange.githubstatus.converter.ModelGsonFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class GsonModule {

  @Provides
  @Singleton
  public Gson providesGson() {
    return new GsonBuilder()
        .registerTypeAdapterFactory(ModelGsonFactory.create())
        .registerTypeAdapterFactory(new LocalDateTimeConverterFactory())
        .create();
  }

}
