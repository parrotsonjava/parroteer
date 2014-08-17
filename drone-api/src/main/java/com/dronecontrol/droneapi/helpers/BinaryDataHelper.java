package com.dronecontrol.droneapi.helpers;

public class BinaryDataHelper {
    public static int getUnsignedByteValue(byte by) {
        return (int) (by & 0xffL);
    }

    public static int getIntValue(byte[] data, int offset, int length) {
        int tempValue;
        int integerValue = 0;

        for (int i = length - 1; i >= 0; i--) {
            integerValue <<= 8;
            tempValue = data[offset + i] & 0xFF;
            integerValue |= tempValue;
        }

        return integerValue;
    }

    public static int[] getIntArray(byte[] data, int offset, int elementLength, int numberOfElements) {
        int[] values = new int[numberOfElements];
        for (int i = 0; i < numberOfElements; i++) {
            values[i] = getIntValue(data, offset + i * elementLength, elementLength);
        }
        return values;
    }

    public static float getFloatValue(byte[] data, int offset, int length) {
        return Float.intBitsToFloat(getIntValue(data, offset, length));
    }

    public static float[] getFloatArray(byte[] data, int offset, int elementLength, int numberOfElements) {
        float[] values = new float[numberOfElements];
        for (int i = 0; i < numberOfElements; i++) {
            values[i] = getFloatValue(data, offset + i * elementLength, elementLength);
        }
        return values;
    }

    public static int getNormalizedIntValue(Float value) {
        if (value < -1.0f) {
            value = -1.0f;
        } else if (value > 1.0f) {
            value = 1.0f;
        }

        return Float.floatToIntBits(value);
    }

    public static boolean flagSet(int flagsValue, int index) {
        return (flagsValue & (1 << index)) != 0;
    }
}
