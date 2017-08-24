package com.deange.githubstatus.model;


import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import org.joda.time.LocalDateTime;


@AutoValue
public abstract class Message {

    @SerializedName("status")
    public abstract State state();

    @SerializedName("body")
    public abstract String body();

    @SerializedName("created_on")
    public abstract LocalDateTime createdOn();

    public static TypeAdapter<Message> typeAdapter(final Gson gson) {
        return new AutoValue_Message.GsonTypeAdapter(gson);
    }

}
