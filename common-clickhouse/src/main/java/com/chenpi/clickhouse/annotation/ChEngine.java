package com.chenpi.clickhouse.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * clickhouse引擎
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ChEngine {
    String value() default "";
}
