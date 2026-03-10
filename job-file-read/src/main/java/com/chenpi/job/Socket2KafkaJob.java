package com.chenpi.job;

import com.chenpi.common.config.EnvConfig;
import com.chenpi.kafka.factory.KafkaSinkFactory;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class Socket2KafkaJob {

    private static final String KAFKA_TOPIC = "read_file_test";

    public static void main(String[] args) throws Exception {

        EnvConfig config = EnvConfig.init();

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> socketSource = env.socketTextStream("localhost", 8888);
        SingleOutputStreamOperator<Tuple2<String, Integer>> sumStream = socketSource
                .flatMap((String line, Collector<Tuple2<String, Integer>> collector) -> {
                    String[] arr = line.split("\\s+");
                    for (String word : arr) {
                        collector.collect(new Tuple2<>(word, 1));
                    }
                })
                .returns(Types.TUPLE(Types.STRING, Types.INT))
                .keyBy(value -> value.f0)
                .sum(1);

        KafkaSink<String> kafkaSink = KafkaSinkFactory.createKafkaSink(
                config.getKafka().getBrokers(),
                "socket-2-kafka-sink",
                KAFKA_TOPIC
        );

        SingleOutputStreamOperator<String> map = sumStream.map(value -> value.f0 + ":" + value.f1);
        map.sinkTo(kafkaSink);
        env.execute(Socket2KafkaJob.class.getSimpleName());
    }
}
