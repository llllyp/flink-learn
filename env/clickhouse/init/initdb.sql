-- 创建 test 数据库
CREATE DATABASE IF NOT EXISTS test
ENGINE = Atomic;  -- 使用 Atomic 引擎（ClickHouse 推荐的默认引擎）

-- 可选：设置默认数据库（方便后续操作）
USE test;