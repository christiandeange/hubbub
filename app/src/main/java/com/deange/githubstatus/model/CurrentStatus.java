package com.deange.githubstatus.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;


@AutoValue
public abstract class CurrentStatus {

    @SerializedName("status")
    abstract State state();

    @SerializedName("last_updated")
    abstract LocalDateTime updatedAt();

    public static TypeAdapter<CurrentStatus> typeAdapter(final Gson gson) {
        return new AutoValue_CurrentStatus.GsonTypeAdapter(gson);
    }

}
