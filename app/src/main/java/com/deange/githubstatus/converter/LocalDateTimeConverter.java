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

    private static final DateTimeFormatter FORMAT = ISODateTimeFormat.localDateOptionalTimeParser();

    @Override
    public void write(final JsonWriter out, final LocalDateTime value) throws IOException {
        out.value(value.toString(FORMAT));
    }

    @Override
    public LocalDateTime read(final JsonReader in) throws IOException {
        return FORMAT.parseLocalDateTime(in.nextString());
    }
}
