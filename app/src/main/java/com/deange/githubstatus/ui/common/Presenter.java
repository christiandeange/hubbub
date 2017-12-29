package com.deange.githubstatus.ui.common;

import android.support.annotation.NonNull;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class Presenter<V> {

  private V view;
  private CompositeDisposable disposables = new CompositeDisposable();
  private Unbinder unbinder = Unbinder.EMPTY;

  public final void takeView(V view) {
    if (view == null) throw new NullPointerException("view must not be null");

    if (this.view != view) {
      if (this.view != null) dropView();

      this.view = view;
      if (this.view instanceof View) {
        unbinder = ButterKnife.bind(this, (View) this.view);
      }
      onLoad(this.view);
    }
  }

  public void dropView() {
    if (this.view != null) {
      this.view = null;
      unbinder.unbind();
      disposables.dispose();
      disposables = new CompositeDisposable();
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