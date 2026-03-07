package com.chenpi.common.config;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
public class EnvConfig {

    private static EnvConfig INSTANCE = null;

    private Mysql mysql;
    private Mongodb mongodb;
    private Redis redis;
    private Clickhouse clickhouse;
    private JobConfig job;

    public static EnvConfig init() {
        if (INSTANCE == null) {
            Yaml yaml = new Yaml();
            try (InputStream inputStream = EnvConfig.class.getClassLoader().getResourceAsStream("job-env.yml")) {
                Objects.requireNonNull(inputStream, "job-env.yml file not found");
                // 先解析为 Map，避免递归调用构造函数
                INSTANCE = yaml.loadAs(inputStream, EnvConfig.class);

            } catch (Exception e) {
                throw new RuntimeException("Failed to load job-env.yml", e);
            }
        }
        return INSTANCE;
    }

    @Data
    public static class Mysql {
        private String database;
        private String host;
        private int port;
        private String username;
        private String password;
    }

    @Data
    public static class Mongodb {
        private String hosts;
        private String database;
        private String username;
        private String password;
    }

    @Data
    public static class Redis {
        private String host;
        private int port;
        private String password;
        private int database;
        private long timeout;
    }

    @Data
    public static class Clickhouse {
        private String database;
        private int max_batch_size;
        private int max_in_flight_requests;
        private int max_buffered_requests;
        private String max_batch_size_in_bytes;
        private String max_time_in_buffer_ms;
        private String max_record_size_in_bytes;

        /**
         * 获取计算后的 maxBatchSizeInBytes 的 long 值
         */
        public Long getMaxBatchSizeInBytesAsLong() {
            if (!max_batch_size_in_bytes.contains("*")) {
                return Long.parseLong(max_batch_size_in_bytes);
            }
            // 如果是表达式，重新计算
            return calculateLongValue(max_batch_size_in_bytes);
        }

        public Long getMaxTimeInBufferMsAsLong() {
            if (!max_time_in_buffer_ms.contains("*")) {
                return Long.parseLong(max_time_in_buffer_ms);
            }
            // 如果是表达式，重新计算
            return calculateLongValue(max_time_in_buffer_ms);
        }

        /**
         * 获取计算后的 maxRecordSizeInBytes 的 long 值
         */
        public Long getMaxRecordSizeInBytesAsLong() {
            if (max_record_size_in_bytes == null) {
                return null;
            }
            try {
                // 如果已经是数字字符串，直接解析
                if (!max_record_size_in_bytes.contains("*")) {
                    return Long.parseLong(max_record_size_in_bytes);
                }
                // 如果是表达式，重新计算
                return calculateLongValue(max_record_size_in_bytes);
            } catch (NumberFormatException e) {
                return null;
            }
        }

        /**
         * 静态方法，计算简单的乘法表达式（如 "1024 * 1024 * 10"）
         */
        private static Long calculateLongValue(String expression) {
            String[] parts = expression.split("\\*");
            long result = 1;
            for (String part : parts) {
                result *= Long.parseLong(part.trim());
            }
            return result;
        }
    }

    @ToString
    public static class JobConfig {
        @Setter
        @Getter
        private List<String> jobIntervalMs;
        @Setter
        @Getter
        private List<String> jobCheckpointIntervalMs;
        @Setter
        @Getter
        private List<String> jobParallelism;

        public JobConfig() {

        }

        @JSONField(serialize = false)
        private Map<String, Long> jobIntervalMsMap;
        @JSONField(serialize = false)
        private Map<String, Long> jobCheckpointIntervalMsMap;
        @JSONField(serialize = false)
        private Map<String, Long> jobParallelismMap;

        public Long getJobInterval(String jobName) {
            if (jobIntervalMsMap == null) {
                jobIntervalMsMap = parseJobConfig(jobIntervalMs);
            }
            return jobIntervalMsMap.get(jobName);
        }

        public Long getJobCheckpointInterval(String jobName) {
            if (jobCheckpointIntervalMsMap == null) {
                jobCheckpointIntervalMsMap = parseJobConfig(jobCheckpointIntervalMs);
            }
            return jobCheckpointIntervalMsMap.get(jobName);
        }

        public Long getJobParallelism(String jobName) {
            if (jobParallelismMap == null) {
                jobParallelismMap = parseJobConfig(jobParallelism);
            }
            return jobParallelismMap.get(jobName);
        }

        // 解析配置项，返回jobName和value的映射
        private Map<String, Long> parseJobConfig(List<String> configList) {
            Map<String, Long> result = new HashMap<>();
            if (configList != null) {
                for (String config : configList) {
                    if (config != null && !config.trim().isEmpty()) {
                        String[] parts = config.split(":");
                        if (parts.length == 2) {
                            String jobName = parts[0].trim();
                            try {
                                long value = Long.parseLong(parts[1].trim());
                                result.put(jobName, value);
                            } catch (NumberFormatException e) {
                                // 忽略解析失败的配置项
                            }
                        }
                    }
                }
            }
            return result;
        }

    }

}
