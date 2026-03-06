package com.chenpi.clickhouse.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 用于标注精度
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Precision {
    /**
     * 整数位数
     */
    int integer() default 18;
    /**
     * 小数位数
     */
    int decimal() default 2;
}
