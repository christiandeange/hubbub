package com.deange.githubstatus.dagger.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.deange.githubstatus.dagger.MockMode;
import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SharedPrefsModule {

  @Provides
  @Singleton
  public static SharedPreferences providesSharedPreferences(Context context) {
    return context.getSharedPreferences("default", Context.MODE_PRIVATE);
  }

  @Provides
  @Singleton
  public static RxSharedPreferences providesRxSharedPreferences(SharedPreferences sharedPrefs) {
    return RxSharedPreferences.create(sharedPrefs);
  }

  @Provides
  @MockMode
  public static Preference<Boolean> providesMockModePreference(RxSharedPreferences preferences) {
    return preferences.getBoolean("mock_mode");
  }

  @Provides
  @MockMode
  public static boolean providesMockModeEnabled(@MockMode Preference<Boolean> preference) {
    return preference.get();
  }

}
