package com.chenpi.kafka.factory;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;

public class KafkaSinkFactory {

    /**
     * 创建KafkaSink
     * @param brokers Kafka broker地址
     * @param transactionalIdPrefix 事务ID前缀
     * @param topic Kafka topic
     * @return KafkaSink
     */
    public static KafkaSink<String> createKafkaSink(String brokers, String transactionalIdPrefix, String topic) {
        return KafkaSink.<String>builder()
                .setBootstrapServers(brokers)
                .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                        .setTopic(topic)
                        .setValueSerializationSchema(new SimpleStringSchema())
                        .build())
                // 核心修正：改为EXACTLY_ONCE，配合CDC精准一次
                .setDeliveryGuarantee(DeliveryGuarantee.EXACTLY_ONCE)
                // 必须设置全局唯一的事务ID前缀，避免多作业/多并发冲突
                .setTransactionalIdPrefix(transactionalIdPrefix + "-" + System.currentTimeMillis())
                // 事务超时时间（必须大于Checkpoint间隔）
                .setProperty("transaction.timeout.ms", "900000") // 15分钟
                // 性能优化：批量发送
                .setProperty("batch.size", "16384")
                .setProperty("linger.ms", "5")
                .build();
    }

    /**
     * 根据  record 规则将数据分发到不同的 topic
     * @param brokers Kafka broker
     * @param topicPrefix topic 前缀
     * @param key 规则 key
     * @param removeSuffix 删除的 key 值的后缀
     * @param transactionalIdPrefix 事务 id 前缀
     * @return KafkaSink
     */
    public static KafkaSink<String> createDynamicTopicKafkaSink(String brokers,
                                                                String topicPrefix,
                                                                String key,
                                                                boolean removeSuffix,
                                                                String transactionalIdPrefix) {
        return KafkaSink.<String>builder()
                .setBootstrapServers(brokers)
                .setRecordSerializer(KafkaRecordSerializationSchema.<String>builder()
                        .setTopicSelector(record -> {
                            // 解析记录，提取表名，生成主题
                            JSONObject jsonObject = JSON.parseObject(record);
                            String value = jsonObject.getString(key);
                            if (removeSuffix) {
                                // xxx_xxx_xxx_24 -> xxx_xxx_xxx
                                value = value.substring(0, value.lastIndexOf("_"));
                            }
                            return topicPrefix + value.toUpperCase() + "_TOPIC";
                        })
                        .setValueSerializationSchema(new SimpleStringSchema())
                        .build())
                .setDeliveryGuarantee(DeliveryGuarantee.EXACTLY_ONCE)
                .setTransactionalIdPrefix(transactionalIdPrefix + "-" + System.currentTimeMillis())
                .setProperty("transaction.timeout.ms", "900000")
                .setProperty("batch.size", "16384")
                .setProperty("linger.ms", "5")
                .build();
    }
}
