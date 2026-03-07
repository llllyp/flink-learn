package com.chenpi.common.config;

public class EnvConfigTest {
    public static void main(String[] args) {
        EnvConfig config = EnvConfig.init();
        System.out.println(config.getJob().getJobInterval("readFile"));
    }
}