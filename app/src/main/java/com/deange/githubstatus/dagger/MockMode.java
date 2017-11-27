package com.deange.githubstatus.dagger;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * When annotating a boolean, returns whether or not mock mode is enabled.
 * When annotating a Preference<Boolean>, returns the backing
 * {@link com.f2prateek.rx.preferences2.Preference Preference} object.
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Qualifier
public @interface MockMode {
}
