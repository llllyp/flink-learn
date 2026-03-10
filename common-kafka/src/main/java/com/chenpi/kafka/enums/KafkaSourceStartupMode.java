package com.chenpi.kafka.enums;

public enum KafkaSourceStartupMode {

    LATEST("latest"),

    EARLIEST("earliest"),

    GROUP_OFFSETS("group-offsets"),

    TIMESTAMP("timestamp"),

    SPECIFIC_OFFSETS("specific-offsets");

    private String mode;

    KafkaSourceStartupMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    public static KafkaSourceStartupMode getMode(String mode) {
        for (KafkaSourceStartupMode value : KafkaSourceStartupMode.values()) {
            if (value.getMode().equals(mode)) {
                return value;
            }
        }
        return null;
    }
}
