package com.chenpi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogBean {
    String ip;      // 访问ip
    int userId;     // 用户id
    long timestamp; // 访问时间戳
    String method;  // 访问方法
    String path;    // 访问路径
}
