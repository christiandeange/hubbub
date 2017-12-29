package com.deange.githubstatus.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import static java.util.Collections.singletonList;

@AutoValue
public abstract class Response
    implements
    Parcelable {

  public static Response create(CurrentStatus status, List<Message> messages) {
    return new AutoValue_Response(status, messages);
  }

  public static Response error(Throwable throwable) {
    return new AutoValue_Response(CurrentStatus.error(), singletonList(Message.error(throwable)));
  }

  @SerializedName("status")
  public abstract CurrentStatus status();

  @SerializedName("messages")
  public abstract List<Message> messages();

  public static TypeAdapter<Response> typeAdapter(Gson gson) {
    return new AutoValue_Response.GsonTypeAdapter(gson);
  }

}
