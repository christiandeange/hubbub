package com.deange.githubstatus.util;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

public final class RxUtils {

  private RxUtils() {
    throw new AssertionError();
  }

  public static <T> ObservableTransformer<T, T> resubscribeWhen(Observable<?> resubscriber) {
    return source ->
        resubscriber.switchMap(a -> source);
  }

}
