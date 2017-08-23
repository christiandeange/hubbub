package com.deange.githubstatus.converter;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

@GsonTypeAdapterFactory
public abstract class ModelGsonFactory
        implements
        TypeAdapterFactory {

    public static TypeAdapterFactory create() {
        return new AutoValueGson_ModelGsonFactory();
    }
}
