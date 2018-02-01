package com.deange.githubstatus.ui.flow;

import android.os.Parcel;
import android.os.Parcelable;

import flow.path.Path;

public abstract class HubbubPath extends Path implements HasLayout, Parcelable {

  @Override
  public int describeContents() {
    // Default to empty implementation
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    // Default to empty implementation
  }

}
