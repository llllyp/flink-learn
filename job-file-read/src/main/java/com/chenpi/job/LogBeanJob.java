package com.chenpi.job;

import com.alibaba.fastjson2.util.DateUtils;
import com.chenpi.model.LogBean;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.TextLineInputFormat;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Date;

public class LogBeanJob {

    public static void main(String[] args) throws Exception {

        //1. env-准备环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC);

        //2. source-加载数据
        FileSource<String> fileSource = FileSource.forRecordStreamFormat(
                new TextLineInputFormat(), new Path("/Users/lyp/learn/flink-learn/.idea/a.log")
        ).build();
        DataStream<String> text = env.fromSource(
                fileSource,
                WatermarkStrategy.noWatermarks(),
                "map-input"
        );

        //3. transformation-数据处理转换
        text.map((MapFunction<String, LogBean>) line -> {
            String[] arr = line.split(" ");
            String timeStr = arr[2];// 17/05/2015:10:05:30
            Date date = DateUtils.parseDate(timeStr, "dd/MM/yyyy:HH:mm:ss");
            long time = date.getTime();

            return new LogBean(arr[0], Integer.parseInt(arr[1]), time, arr[3], arr[4]);
        }).print();

        //4. sink-数据输出

        //5. execute-执行
        env.execute(LogBeanJob.class.getSimpleName());
    }
}