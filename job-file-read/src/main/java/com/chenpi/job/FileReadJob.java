package com.chenpi.job;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.TextLineInputFormat;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class FileReadJob {

    public static void main(String[] args) throws Exception {
        // 创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        
        // 配置文件源
        FileSource<String> fileSource = FileSource.forRecordStreamFormat(
                new TextLineInputFormat(),
                new Path("/path/to/input/file.txt")
        ).build();
        
        // 读取文件数据
        DataStream<String> lines = env.fromSource(
                fileSource,
                WatermarkStrategy.noWatermarks(),
                "File Source"
        );
        
        // 简单处理：统计单词数
        DataStream<Tuple2<String, Integer>> wordCounts = lines
                .flatMap((String line, org.apache.flink.util.Collector<Tuple2<String, Integer>> collector) -> {
                    for (String word : line.split("\\s")) {
                        collector.collect(new Tuple2<>(word, 1));
                    }
                })
                .returns(org.apache.flink.api.common.typeinfo.Types.TUPLE(Types.STRING, Types.INT))
                .keyBy(value -> value.f0)
                .sum(1);
        
        // 输出结果
        wordCounts.print();
        
        // 执行作业
        env.execute("File Read Job");
    }
}