package com.chenpi.source;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

import com.chenpi.model.OrderInfo;

import lombok.Data;

@Data
public class MySource implements SourceFunction<OrderInfo> {

    boolean flag = true;

     @Override
    public void run(SourceContext<OrderInfo> ctx) throws Exception {
        // 源源不断产生数据
        Random random = new Random();
        while (flag) {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderId(String.valueOf(random.nextInt(1000000)));
            orderInfo.setUid(random.nextInt(1000000));
            orderInfo.setPrice(random.nextInt(1000000));
            orderInfo.setTimeStemp(System.currentTimeMillis());
            TimeUnit.SECONDS.sleep(1);
            ctx.collect(orderInfo);
        }
        
    }

    // Source 停止前要干啥
    @Override
    public void cancel() {
        flag = false;
    }

}
