package com.deange.githubstatus.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

@AutoValue
public abstract class Response
    implements
    Parcelable {

  public static Response create(final CurrentStatus status, final List<Message> messages) {
    return new AutoValue_Response(status, messages);
  }

  public static Response error(final Throwable throwable) {
    return new AutoValue_Response(
        CurrentStatus.error(), Collections.singletonList(Message.error(throwable)));
  }

  @SerializedName("status")
  public abstract CurrentStatus status();

  @SerializedName("messages")
  public abstract List<Message> messages();

  public static TypeAdapter<Response> typeAdapter(final Gson gson) {
    return new AutoValue_Response.GsonTypeAdapter(gson);
  }

}
