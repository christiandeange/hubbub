package com.deange.githubstatus.util;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * String-based {@linkplain Qualifier qualifier}.
 *
 * <p>Example usage:
 *
 * <pre>
 *   public class Presenter {
 *     &#064;Inject <b>@RxPreference("driver")</b> Preference driverSeat;
 *     &#064;Inject <b>@RxPreference("passenger")</b> Preference passengerSeat;
 *     ...
 *   }</pre>
 */
@Qualifier
@Documented
@Retention(RUNTIME)
public @interface RxPreference {

    /** The name. */
    String value() default "";
}
