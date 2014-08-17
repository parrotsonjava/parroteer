package com.dronecontrol.droneapi.data.enums;

public enum DetectionType {

    CIRCULAR_ROUNDEL(12);

    private final int value;

    DetectionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}