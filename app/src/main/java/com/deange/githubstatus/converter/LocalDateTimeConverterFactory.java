package com.deange.githubstatus.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import org.joda.time.LocalDateTime;

public class LocalDateTimeConverterFactory
    implements
    TypeAdapterFactory {

  @SuppressWarnings("unchecked")
  @Override
  public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
    Class<T> rawType = (Class<T>) type.getRawType();
    if (LocalDateTime.class.isAssignableFrom(rawType)) {
      return (TypeAdapter<T>) new LocalDateTimeConverter().nullSafe();
    } else {
      return null;
    }
  }

}
