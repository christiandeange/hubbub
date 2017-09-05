package com.deange.githubstatus.model;


import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import org.joda.time.LocalDateTime;


@AutoValue
public abstract class Message {

    @SerializedName("status")
    public abstract State state();

    @Nullable
    @SerializedName("body")
    public abstract String body();

    @SerializedName("created_on")
    public abstract LocalDateTime createdOn();

    @Memoized
    public long id() {
        return createdOn().toDate().getTime();
    }

    public static TypeAdapter<Message> typeAdapter(final Gson gson) {
        return new AutoValue_Message.GsonTypeAdapter(gson);
    }

}
