package com.chenpi.kafka.factory;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;

import java.util.regex.Pattern;

public class KafkaSourceFactory {

    /**
     * 匹配指定 topic 的 kafka source
     * @param brokers Kafka broker
     * @param startupMode 启动模式
     * @param startupTimestampMillis timestamp 模式启动时间
     * @param consumerGroupId 消费组ID
     * @param topic  topic
     * @return KafkaSource
     */
    public static KafkaSource<String> createKafkaSource(String brokers,
                                                        String startupMode,
                                                        long startupTimestampMillis,
                                                        String consumerGroupId,
                                                        String... topic
    ) {
        return KafkaSource.<String>builder()
                .setBootstrapServers(brokers)
                .setTopics(topic)
                .setGroupId(consumerGroupId)
                .setStartingOffsets(getStartingOffsets(startupMode, startupTimestampMillis))
                .setDeserializer(KafkaRecordDeserializationSchema.valueOnly(new SimpleStringSchema()))
                .build();
    }

    /**
     * 匹配 topicPrefix 前缀的 topic
     * @param brokers Kafka broker
     * @param startupMode 启动模式
     * @param startupTimestampMillis timestamp 模式起始时间
     * @param consumerGroupId 消费组ID
     * @param topicPrefix topic前缀
     * @return KafkaSource
     */
    public static KafkaSource<String> createDynamicTopicKafkaSource(String brokers,
                                                                    String startupMode,
                                                                    Long startupTimestampMillis,
                                                                    String consumerGroupId,
                                                                    String topicPrefix
    ) {
        return KafkaSource.<String>builder()
                .setBootstrapServers(brokers)
                .setTopicPattern(Pattern.compile(topicPrefix + ".*")) // 使用正则表达式匹配以指定前缀开头的所有主题
                .setGroupId(consumerGroupId)
                .setStartingOffsets(getStartingOffsets(startupMode, startupTimestampMillis))
                .setDeserializer(KafkaRecordDeserializationSchema.valueOnly(new SimpleStringSchema()))
                .build();
    }

    private static OffsetsInitializer getStartingOffsets(String startupMode, Long startupTimestampMillis) {
        return switch (startupMode) {
            case "committed_strict" -> OffsetsInitializer.committedOffsets(OffsetResetStrategy.NONE);
            case "committed_earliest" -> OffsetsInitializer.committedOffsets(OffsetResetStrategy.EARLIEST);
            // 从 checkpoint/savepoint 恢复时，会从指定的 offset 开始消费 否则从头开始消费
            case "earliest" -> OffsetsInitializer.earliest();
            case "latest" -> OffsetsInitializer.latest();
            case "timestamp" -> OffsetsInitializer.timestamp(startupTimestampMillis);
            default -> throw new IllegalArgumentException("Unsupported kafka.startupMode: " + startupMode);
        };
    }
}
