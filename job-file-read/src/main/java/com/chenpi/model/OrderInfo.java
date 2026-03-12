package com.chenpi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo {

    private String orderId;

    private int uid;

    private int price;

    private long timeStemp;

}
