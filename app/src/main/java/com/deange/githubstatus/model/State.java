package com.deange.githubstatus.model;

import com.google.gson.annotations.SerializedName;

public enum State {

    @SerializedName("good")
    GOOD,

    @SerializedName("minor")
    MINOR,

    @SerializedName("major")
    MAJOR,

}
