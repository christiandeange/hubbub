package com.deange.githubstatus.ui;

import android.app.ActivityManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.ColorInt;

import com.deange.githubstatus.R;
import com.deange.githubstatus.ui.common.BaseActivity;

import javax.inject.Inject;

import static com.deange.githubstatus.MainApplication.component;

public class MainActivity
    extends BaseActivity {

  @Inject MainPresenter presenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setTitle(null);

    component(this).inject(this);

    presenter.takeView((MainView) findViewById(R.id.main_view));
    setSupportActionBar(presenter.toolbar());
    unsubscribeOnDestroy(presenter.onColorUpdated().subscribe(this::updateColor));
  }

  void updateColor(@ColorInt int color) {
    getWindow().setStatusBarColor(color);

    setTaskDescription(new ActivityManager.TaskDescription(
        getString(R.string.app_name),
        BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher),
        color)
    );
  }

}
