package com.dronecontrol.droneapi.data.enums;

public enum TagOption {

    NAV_DATA(0),
    VISION_DETECT_DATA(16);

    private int value;

    private TagOption(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static int getBitMaskFor(TagOption[] options) {
        int mask = NAV_DATA.getValue();     // Nav data is always included
        for (TagOption option : options) {
            mask = mask | 1 << option.getValue();
        }
        return mask;
    }
}