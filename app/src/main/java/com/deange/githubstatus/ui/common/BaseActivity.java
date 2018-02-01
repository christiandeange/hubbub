package com.deange.githubstatus.ui.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.deange.githubstatus.R;
import com.deange.githubstatus.ui.flow.HasLayout;
import com.deange.githubstatus.ui.flow.HubbubPathParceler;

import flow.Flow;
import flow.FlowDelegate;
import flow.History;
import flow.path.Path;
import flow.path.PathContainerView;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static mortar.bundler.BundleServiceRunner.getBundleServiceRunner;


public abstract class BaseActivity
    extends AppCompatActivity implements Flow.Dispatcher, HasLayout {

  private final CompositeDisposable disposables = new CompositeDisposable();

  private PathContainerView container;
  protected MortarScope activityScope;
  protected FlowDelegate flowDelegate;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    int layoutId = layoutId();
    if (layoutId != 0) {
      setContentView(layoutId);
    }
    container = findViewById(R.id.container);

    MortarScope appScope = MortarScope.getScope(getApplication());
    String scopeName = getLocalClassName();
    activityScope = appScope.findChild(scopeName);
    if (activityScope == null) {
      activityScope =
          appScope.buildChild()
                  .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
                  .build(scopeName);
    }
    getBundleServiceRunner(activityScope).onCreate(savedInstanceState);

    FlowDelegate.NonConfigurationInstance nonConfig =
        (FlowDelegate.NonConfigurationInstance) getLastCustomNonConfigurationInstance();
    flowDelegate = FlowDelegate.onCreate(
        nonConfig, getIntent(), savedInstanceState, HubbubPathParceler.INSTANCE,
        History.single(defaultPath()), this);
  }

  @Override
  public void dispatch(Flow.Traversal traversal, Flow.TraversalCallback callback) {
    container.dispatch(traversal, callback);
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    flowDelegate.onNewIntent(intent);
  }

  @Override
  protected void onResume() {
    super.onResume();
    flowDelegate.onResume();
  }

  @Override
  protected void onPause() {
    flowDelegate.onPause();
    super.onPause();
  }

  @Override
  public Object onRetainCustomNonConfigurationInstance() {
    return flowDelegate.onRetainNonConfigurationInstance();
  }

  @Override
  public Object getSystemService(@NonNull String name) {
    if (flowDelegate != null) {
      Object flowService = flowDelegate.getSystemService(name);
      if (flowService != null) return flowService;
    }

    return activityScope != null && activityScope.hasService(name)
        ? activityScope.getService(name)
        : super.getSystemService(name);
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    flowDelegate.onSaveInstanceState(outState);
    getBundleServiceRunner(this).onSaveInstanceState(outState);
  }

  @Override
  public void onDestroy() {
    if (isFinishing() && activityScope != null) {
      activityScope.destroy();
      activityScope = null;
    }

    disposables.dispose();
    super.onDestroy();
  }

  @NonNull
  protected abstract Path defaultPath();

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  public void unsubscribeOnDestroy(Disposable disposable) {
    disposables.add(disposable);
  }
}
