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
    public SharedPreferences providesSharedPreferences(final Context context) {
        return context.getSharedPreferences("default", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    public RxSharedPreferences providesRxSharedPreferences(final SharedPreferences sharedPrefs) {
        return RxSharedPreferences.create(sharedPrefs);
    }

    @Provides
    @MockMode
    public Preference<Boolean> providesMockModePreference(final RxSharedPreferences preferences) {
        return preferences.getBoolean("mock_mode");
    }

    @Provides
    @MockMode
    public boolean providesMockModeEnabled(@MockMode final Preference<Boolean> preference) {
        return preference.get();
    }

}
