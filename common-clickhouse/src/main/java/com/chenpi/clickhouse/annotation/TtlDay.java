package com.chenpi.clickhouse.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Clickhouse 数据过期时间(日维度)
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TtlDay {
    int value() default -1;
}
