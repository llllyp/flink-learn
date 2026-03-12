package com.chenpi.job;

import com.chenpi.source.MySource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class CustomSourceJob {

    public static void main(String[] args) throws Exception {
        
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);

        env.addSource(new MySource())
            .print();

        env.execute(CustomSourceJob.class.getSimpleName());
    }

}
