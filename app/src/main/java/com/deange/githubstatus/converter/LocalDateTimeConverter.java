package com.deange.githubstatus.converter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;

class LocalDateTimeConverter
    extends TypeAdapter<LocalDateTime> {

  private static final DateTimeFormatter ISO_FORMAT = ISODateTimeFormat.dateTimeParser();

  @Override
  public void write(JsonWriter out, LocalDateTime value) throws IOException {
    out.value(value.toDateTime().toString(ISO_FORMAT));
  }

  @Override
  public LocalDateTime read(JsonReader in) throws IOException {
    return ISO_FORMAT.parseDateTime(in.nextString()).toLocalDateTime();
  }
}
