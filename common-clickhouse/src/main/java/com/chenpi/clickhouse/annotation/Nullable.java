package com.chenpi.clickhouse.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 标注 Clickhouse 宽表 列是否可以为空  分区和排序的列不能为空,不允许标注
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Nullable {
}
