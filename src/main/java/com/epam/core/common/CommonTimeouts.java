package com.epam.core.common;

public enum CommonTimeouts {
    TIMEOUT_500_MS(500),
    TIMEOUT_1_S(1L),
    TIMEOUT_2_S(2L),
    TIMEOUT_3_S(3L),
    TIMEOUT_5_S(5L),
    TIMEOUT_7_S(7L),
    TIMEOUT_10_S(10L),
    DEFAULT_ELEMENT_WAITING_TIMEOUT(30L),
    DEFAULT_WAIT_TO_LOADING_TIMEOUT(60L);

    private int milliseconds;

    private CommonTimeouts(Long seconds) {
        this.milliseconds = (int) (seconds * 1000);
    }

    private CommonTimeouts(Integer milliseconds) {
        this.milliseconds = milliseconds;
    }

    public int getSeconds() {
        return this.milliseconds / 1000;
    }

    public int getMilliSeconds() {
        return milliseconds;
    }
}
