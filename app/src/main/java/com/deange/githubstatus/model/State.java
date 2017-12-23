package com.deange.githubstatus.model;

import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;

import com.deange.githubstatus.R;
import com.google.gson.annotations.SerializedName;

public enum State {

  @SerializedName("good")
  GOOD(
      R.string.state_good,
      R.string.state_good_description,
      R.color.state_good),

  @SerializedName("minor")
  MINOR(
      R.string.state_minor,
      R.string.state_minor_description,
      R.color.state_minor),

  @SerializedName("major")
  MAJOR(
      R.string.state_major,
      R.string.state_major_description,
      R.color.state_major),

  @SerializedName("error")
  ERROR(
      R.string.state_error,
      R.string.state_error_description,
      R.color.state_error),;

  @StringRes private final int titleResId;
  @StringRes private final int descriptionResId;
  @ColorRes private final int colorResId;

  State(
      @StringRes final int titleResId,
      @StringRes final int descriptionResId,
      @ColorRes final int colorResId) {
    this.titleResId = titleResId;
    this.descriptionResId = descriptionResId;
    this.colorResId = colorResId;
  }

  @StringRes
  public int getTitleResId() {
    return titleResId;
  }

  @StringRes
  public int getDescriptionResId() {
    return descriptionResId;
  }

  @ColorRes
  public int getColorResId() {
    return colorResId;
  }

  public int getWeight() {
    return (this == ERROR) ? 2 : ordinal() + 1;
  }
}
