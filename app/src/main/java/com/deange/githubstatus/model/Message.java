package com.deange.githubstatus.model;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import org.joda.time.LocalDateTime;

import static com.deange.githubstatus.model.State.ERROR;

@AutoValue
public abstract class Message
    implements
    Parcelable {

  public static Message create(final State state, final String body, final LocalDateTime time) {
    return new AutoValue_Message(state, body, time);
  }

  public static Message error(final Throwable throwable) {
    return new AutoValue_Message(ERROR, throwable.getLocalizedMessage(), LocalDateTime.now());
  }

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

  public String bodyForNotification(final Context context) {
    String body = body();

    if (body == null) {
      return context.getString(state().getDescriptionResId());
    }

    return body;
  }

  public static TypeAdapter<Message> typeAdapter(final Gson gson) {
    return new AutoValue_Message.GsonTypeAdapter(gson);
  }
}
