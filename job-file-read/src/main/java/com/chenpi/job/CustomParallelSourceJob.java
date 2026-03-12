package com.chenpi.job;

import com.chenpi.source.MyParallelSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class CustomParallelSourceJob {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(6);

        env.addSource(new MyParallelSource()).print();

        env.execute(CustomParallelSourceJob.class.getSimpleName());
    }
}
