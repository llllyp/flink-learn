package com.chenpi.clickhouse.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 标准表名, 用于clickhouse 创建表, 以及 cdc 表数据解析
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TableName {

     String name() default "";

     String comment() default "";
}
