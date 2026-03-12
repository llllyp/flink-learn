package com.chenpi.source;

import org.apache.flink.streaming.api.functions.source.ParallelSourceFunction;

import java.util.UUID;

public class MyParallelSource implements ParallelSourceFunction<String> {
    @Override
    public void run(SourceContext<String> ctx) {
        ctx.collect(UUID.randomUUID().toString());
    }

    @Override
    public void cancel() {

    }
}
