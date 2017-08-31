package com.deange.githubstatus.dagger.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.deange.githubstatus.util.RxPreference;
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
    @RxPreference("topic")
    public Preference<String> providesTopicPreference(final RxSharedPreferences sharedPrefs) {
        return sharedPrefs.getString("topic", "");
    }

}
