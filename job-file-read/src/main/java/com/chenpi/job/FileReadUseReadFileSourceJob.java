package com.chenpi.job;

import org.apache.flink.api.java.io.TextInputFormat;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/*
JDK 17 反射异常, 添加 JVM 参数
--add-opens=java.base/java.lang=ALL-UNNAMED
--add-opens=java.base/java.nio=ALL-UNNAMED
--add-opens=java.base/java.util=ALL-UNNAMED
--add-opens=java.base/sun.nio.ch=ALL-UNNAMED
 */
public class FileReadUseReadFileSourceJob {

    private static final String filePath = "/Users/lyp/learn/flink-learn/env/example/word_test.txt";

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> fileSource = env.readTextFile(filePath);
        fileSource.print();


        TextInputFormat inputFormat = new TextInputFormat(new Path(filePath));
        DataStreamSource<String> dataStream = env.readFile(inputFormat, filePath);
        dataStream.print();

        env.execute();

    }
}
