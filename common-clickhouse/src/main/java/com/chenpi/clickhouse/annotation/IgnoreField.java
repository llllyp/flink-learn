package com.chenpi.clickhouse.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * clickhouse建表 标注是否要去掉该项
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreField {
}
