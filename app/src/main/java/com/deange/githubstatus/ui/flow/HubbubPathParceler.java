package com.deange.githubstatus.ui.flow;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;

import flow.StateParceler;

public class HubbubPathParceler implements StateParceler {

  public static final HubbubPathParceler INSTANCE = new HubbubPathParceler();

  @Override
  public Parcelable wrap(Object instance) {
    return (HubbubPath) instance;
  }

  @Override
  public Object unwrap(Parcelable parcelable) {
    return parcelable;
  }

  public static <T extends HubbubPath> Parcelable.Creator<T> forInstance(T instance) {
    return new Parcelable.Creator<T>() {
      @Override
      public T createFromParcel(Parcel source) {
        return instance;
      }

      @Override
      public T[] newArray(int size) {
        //noinspection unchecked
        return (T[]) Array.newInstance(instance.getClass(), size);
      }
    };
  }

}
