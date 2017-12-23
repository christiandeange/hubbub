package com.deange.githubstatus.util;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import java.util.Iterator;

import dagger.internal.Preconditions;

public class ViewGroupIterable implements Iterable<View> {

  private final ViewGroup parent;

  public static ViewGroupIterable on(final ViewGroup parent) {
    Preconditions.checkNotNull(parent, "parent == null");
    return new ViewGroupIterable(parent);
  }

  private ViewGroupIterable(final ViewGroup parent) {
    this.parent = parent;
  }

  @NonNull
  @Override
  public Iterator<View> iterator() {
    return new ViewGroupIterator(parent);
  }

  private static class ViewGroupIterator
      implements
      Iterator<View> {

    private final ViewGroup parent;
    private int index;

    public ViewGroupIterator(final ViewGroup parent) {
      this.parent = parent;
    }

    @Override
    public boolean hasNext() {
      return parent != null && index < parent.getChildCount();
    }

    @Override
    public View next() {
      return parent.getChildAt(index++);
    }
  }

}
