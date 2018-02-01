package com.deange.githubstatus.ui.scoping;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import flow.path.Path;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Applies a rule to enter a subcomponent scope when showing a {@link Path}.
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface WithComponent {
  Class<?> value();
}
