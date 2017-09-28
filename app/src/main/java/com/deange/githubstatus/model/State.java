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
            R.color.state_major);

    @StringRes private final int mTitleResId;
    @StringRes private final int mDescriptionResId;
    @ColorRes private final int mColorResId;

    State(
            @StringRes final int titleResId,
            @StringRes final int descriptionResId,
            @ColorRes final int colorResId) {
        mTitleResId = titleResId;
        mDescriptionResId = descriptionResId;
        mColorResId = colorResId;
    }

    @StringRes
    public int getTitleResId() {
        return mTitleResId;
    }

    @StringRes
    public int getDescriptionResId() {
        return mDescriptionResId;
    }

    @ColorRes
    public int getColorResId() {
        return mColorResId;
    }

    public int getWeight() {
        return ordinal() + 1;
    }
}
