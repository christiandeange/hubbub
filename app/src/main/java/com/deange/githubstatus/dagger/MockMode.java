package com.deange.githubstatus.dagger;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * When annotating a Preference<Boolean>, returns the backing
 * {@link com.f2prateek.rx.preferences2.Preference Preference} object.
 */
@Retention(RUNTIME)
@Documented
@Qualifier
public @interface MockMode {
}
