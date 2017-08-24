package com.deange.githubstatus.model;

import android.support.annotation.StringRes;

import com.deange.githubstatus.R;
import com.google.gson.annotations.SerializedName;

public enum State {

    @SerializedName("good")
    GOOD(R.string.state_good),

    @SerializedName("minor")
    MINOR(R.string.state_minor),

    @SerializedName("major")
    MAJOR(R.string.state_major);

    @StringRes private final int mStringResId;

    State(@StringRes final int stringResId) {
        mStringResId = stringResId;
    }

    @StringRes
    public int getStringResId() {
        return mStringResId;
    }
}
