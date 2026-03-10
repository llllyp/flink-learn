package com.chenpi.job;

import com.chenpi.common.config.EnvConfig;
import com.chenpi.kafka.enums.KafkaSourceStartupMode;
import com.chenpi.kafka.factory.KafkaSourceFactory;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class ConsumerKafkaJob {

    private static final String KAFKA_TOPIC = "read_file_test";

    public static void main(String[] args) throws Exception {
        EnvConfig config = EnvConfig.init();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 创建 Kafka 数据源
        KafkaSource<String> kafkaSource = KafkaSourceFactory.createKafkaSource(config.getKafka().getBrokers(),
                KafkaSourceStartupMode.EARLIEST.getMode(),
                0L,
                "test-consumer",
                KAFKA_TOPIC
        );

        // 打印 Kafka 数据
        env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "Kafka Source")
                .print();

        env.execute(ConsumerKafkaJob.class.getSimpleName());
    }
}
