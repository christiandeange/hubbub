package com.deange.githubstatus.ui.common;

import android.support.annotation.NonNull;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class Presenter<V> {

  private V view;
  private CompositeDisposable disposables;

  public final void takeView(V view) {
    if (view == null) throw new NullPointerException("view must not be null");

    if (this.view != view) {
      if (this.view != null) dropView();

      this.view = view;
      disposables = new CompositeDisposable();
      onLoad(this.view);
    }
  }

  public void dropView() {
    if (this.view != null) {
      this.view = null;
      disposables.dispose();
      onUnload();
    }
  }

  protected final void unsubscribeOnUnload(@NonNull Disposable disposable) {
    disposables.add(disposable);
  }

  protected final V getView() {
    return view;
  }

  protected final boolean hasView() {
    return view != null;
  }

  protected abstract void onLoad(V view);

  protected void onUnload() {
  }

}