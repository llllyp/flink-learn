package com.chenpi.clickhouse.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * clickhouse  排序列
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderBy {
    String value() default  "";
}
