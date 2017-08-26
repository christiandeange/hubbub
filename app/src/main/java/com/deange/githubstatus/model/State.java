package com.deange.githubstatus.model;

import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;

import com.deange.githubstatus.R;
import com.google.gson.annotations.SerializedName;

public enum State {

    @SerializedName("good")
    GOOD(R.string.state_good, R.color.state_good),

    @SerializedName("minor")
    MINOR(R.string.state_minor, R.color.state_minor),

    @SerializedName("major")
    MAJOR(R.string.state_major, R.color.state_major);

    @StringRes private final int mStringResId;
    @ColorRes private final int mColorResId;

    State(@StringRes final int stringResId, @ColorRes final int colorResId) {
        mStringResId = stringResId;
        mColorResId = colorResId;
    }

    @StringRes
    public int getStringResId() {
        return mStringResId;
    }

    @ColorRes
    public int getColorResId() {
        return mColorResId;
    }
}
