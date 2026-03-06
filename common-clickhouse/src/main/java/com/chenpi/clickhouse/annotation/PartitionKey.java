package com.chenpi.clickhouse.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Clickhouse 分区
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface PartitionKey {
    String value() default  "";
}
