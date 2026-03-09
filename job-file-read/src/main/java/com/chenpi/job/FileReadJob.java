package com.chenpi.job;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.TextLineInputFormat;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import static org.apache.flink.api.common.typeinfo.Types.TUPLE;

public class FileReadJob {

//    private static final String filePath = "C:\\Users\\x\\Documents\\word_test.txt";

    public static void main(String[] args) throws Exception {

        ParameterTool param = ParameterTool.fromArgs(args);
        String filePath = param.get("input");

        // 创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        
        // 配置文件源
        FileSource<String> fileSource = FileSource.forRecordStreamFormat(
                new TextLineInputFormat(),
                new Path(filePath)
        ).build();
        
        // 读取文件数据
        DataStream<String> lines = env.fromSource(
                fileSource,
                WatermarkStrategy.noWatermarks(),
                "File Source"
        );
        
        // 简单处理：统计单词数
        DataStream<Tuple2<String, Integer>> wordCounts = lines
                .flatMap((String line, Collector<Tuple2<String, Integer>> collector) -> {
                    for (String word : line.split(" ")) {
                        collector.collect(new Tuple2<>(word, 1));
                    }
                })
                .returns(TUPLE(Types.STRING, Types.INT))
                .keyBy(value -> value.f0)
                .sum(1);
        
        // 输出结果
        wordCounts.print();
        
        // 执行作业
        env.execute("File Read Job");
    }
}