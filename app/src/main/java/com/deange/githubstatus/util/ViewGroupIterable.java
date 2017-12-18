package com.deange.githubstatus.util;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import java.util.Iterator;

import dagger.internal.Preconditions;

public class ViewGroupIterable implements Iterable<View> {

  private final ViewGroup mParent;

  public static ViewGroupIterable on(final ViewGroup parent) {
    Preconditions.checkNotNull(parent, "parent == null");
    return new ViewGroupIterable(parent);
  }

  private ViewGroupIterable(final ViewGroup parent) {
    mParent = parent;
  }

  @NonNull
  @Override
  public Iterator<View> iterator() {
    return new ViewGroupIterator(mParent);
  }

  private static class ViewGroupIterator
      implements
      Iterator<View> {

    private final ViewGroup mParent;
    private int mIndex;

    public ViewGroupIterator(final ViewGroup parent) {
      mParent = parent;
    }

    @Override
    public boolean hasNext() {
      return mParent != null && mIndex < mParent.getChildCount();
    }

    @Override
    public View next() {
      return mParent.getChildAt(mIndex++);
    }
  }

}
