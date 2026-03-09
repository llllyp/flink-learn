package com.chenpi.job;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.List;

public class CollectionSourceJob {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        List<String> list = List.of("1", "2", "3");
        DataStreamSource<String> dataSource1 = env.fromCollection(list);
        dataSource1.print();

        DataStreamSource<String> dataSource2 = env.fromElements("hello", "java");
        dataSource2.print();

        DataStreamSource<Long> dataSource3 = env.fromSequence(1, 100);
        dataSource3.print();

        env.execute(CollectionSourceJob.class.getSimpleName());
    }
}
