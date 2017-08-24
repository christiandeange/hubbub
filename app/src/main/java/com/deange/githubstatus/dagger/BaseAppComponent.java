package com.deange.githubstatus.dagger;


import com.deange.githubstatus.MainApplication;
import com.deange.githubstatus.ui.MainActivity;

public interface BaseAppComponent {

    void inject(final MainApplication target);

    void inject(final MainActivity target);
}
