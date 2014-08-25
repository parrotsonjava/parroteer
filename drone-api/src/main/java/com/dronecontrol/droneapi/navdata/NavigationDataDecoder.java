package com.dronecontrol.droneapi.navdata;

import com.dronecontrol.droneapi.data.NavData;
import com.dronecontrol.droneapi.data.NavDataState;
import com.dronecontrol.droneapi.data.VisionData;
import com.dronecontrol.droneapi.data.VisionTagData;
import com.dronecontrol.droneapi.data.enums.ControlAlgorithm;
import com.dronecontrol.droneapi.data.enums.TagOption;
import com.google.common.collect.ImmutableList;

import static com.dronecontrol.droneapi.helpers.BinaryDataHelper.*;
import static com.google.common.base.Preconditions.checkState;

public class NavigationDataDecoder {
    private final static int CORRECT_HEADER = 0x55667788;

    private static final int NAV_DATA_TAG_CHECKSUM = 0xFFFF;

    private static final int MAX_NUMBER_OF_TAGS = 4;

    private int currentOffset;

    private byte[] buffer;

    private int bufferLength;

    private NavData currentNavData;

    public NavData getNavDataFrom(byte[] buffer, int bufferLength) {
        initializeFields(buffer, bufferLength);

        processNavDataHeader(buffer);
        currentOffset += 16;

        while (currentOffset < bufferLength) {
            int tag = getIntValue(buffer, currentOffset, 2);
            int length = getIntValue(buffer, currentOffset + 2, 2);
            currentOffset += 4;

            checkState(length != 0, "Got a zero length tag");
            processTag(tag, length);

            currentOffset += length - 4;
        }

        return currentNavData;
    }

    private void initializeFields(byte[] buffer, int bufferLength) {
        this.buffer = buffer;
        this.bufferLength = bufferLength;
        this.currentOffset = 0;
        this.currentNavData = new NavData();
    }

    private void processNavDataHeader(byte[] buffer) {
        checkState(isHeaderCorrect(), "The header is incorrect");
        int stateFlags = getIntValue(buffer, currentOffset + 4, 4);
        int sequenceNumber = getIntValue(buffer, currentOffset + 8, 4);

        currentNavData.setSequenceNumber(sequenceNumber);
        currentNavData.setState(getNavDataState(stateFlags));
    }

    private boolean isHeaderCorrect() {
        int header = getIntValue(buffer, 0, 4);
        return header == CORRECT_HEADER;
    }

    public NavDataState getNavDataState(int stateFlags) {
        NavDataState state = new NavDataState();

        state.setFlying(flagSet(stateFlags, 0));
        state.setVideoEnabled(flagSet(stateFlags, 1));
        state.setVisionEnabled(flagSet(stateFlags, 2));
        state.setControlAlgorithm(flagSet(stateFlags, 3) ? ControlAlgorithm.ANGULAR_SPEED_CONTROL : ControlAlgorithm.EULER_ANGLES_CONTROL);
        state.setAltitudeControlActive(flagSet(stateFlags, 4));
        state.setUserFeedbackOn(flagSet(stateFlags, 5));
        state.setControlReceived(flagSet(stateFlags, 6));
        state.setTrimReceived(flagSet(stateFlags, 7));
        state.setTrimRunning(flagSet(stateFlags, 8));
        state.setTrimSucceeded(flagSet(stateFlags, 9));
        state.setNavDataDemoOnly(flagSet(stateFlags, 10));
        state.setNavDataBootstrap(flagSet(stateFlags, 11));
        state.setMotorsDown(flagSet(stateFlags, 12));
        state.setGyrometersDown(flagSet(stateFlags, 14));
        state.setBatteryTooLow(flagSet(stateFlags, 15));
        state.setBatteryTooHigh(flagSet(stateFlags, 16));
        state.setTimerElapsed(flagSet(stateFlags, 17));
        state.setNotEnoughPower(flagSet(stateFlags, 18));
        state.setAnglesOutOfRange(flagSet(stateFlags, 19));
        state.setTooMuchWind(flagSet(stateFlags, 20));
        state.setUltrasonicSensorDeaf(flagSet(stateFlags, 21));
        state.setCutoutSystemDetected(flagSet(stateFlags, 22));
        state.setPicVersionNumberOK(flagSet(stateFlags, 23));
        state.setAtCodedThreadOn(flagSet(stateFlags, 24));
        state.setNavDataThreadOn(flagSet(stateFlags, 25));
        state.setVideoThreadOn(flagSet(stateFlags, 26));
        state.setAcquisitionThreadOn(flagSet(stateFlags, 27));
        state.setControlWatchdogDelayed(flagSet(stateFlags, 28));
        state.setAdcWatchdogDelayed(flagSet(stateFlags, 29));
        state.setCommunicationProblemOccurred(flagSet(stateFlags, 30));
        state.setEmergency(flagSet(stateFlags, 31));

        return state;
    }

    private void processTag(int tag, int length) {
        if (tag == TagOption.NAV_DATA.getValue()) {
            currentNavData.setOnlyHeaderPresent(false);
            processNavData();
        } else if (tag == TagOption.VISION_DETECT_DATA.getValue()) {
            currentNavData.setOnlyHeaderPresent(false);
            processVisionData();
        } else if (tag == NAV_DATA_TAG_CHECKSUM) {
            processCheckSum(length);
        }
    }

    private void processVisionData() {
        int numberOfDetectedTags = getIntValue(buffer, currentOffset, 4);

        int[] xValues = getIntArray(buffer, currentOffset + 20, 4, MAX_NUMBER_OF_TAGS);
        int[] yValues = getIntArray(buffer, currentOffset + 36, 4, MAX_NUMBER_OF_TAGS);
        int[] widthValues = getIntArray(buffer, currentOffset + 52, 4, MAX_NUMBER_OF_TAGS);
        int[] heightValues = getIntArray(buffer, currentOffset + 68, 4, MAX_NUMBER_OF_TAGS);
        int[] distanceValues = getIntArray(buffer, currentOffset + 84, 4, MAX_NUMBER_OF_TAGS);
        float[] orientationValues = getFloatArray(buffer, currentOffset + 82, 4, MAX_NUMBER_OF_TAGS);

        ImmutableList.Builder<VisionTagData> tagDataBuilder = new ImmutableList.Builder<>();
        for (int i = 0; i < numberOfDetectedTags; i++) {
            tagDataBuilder.add(
                    new VisionTagData(xValues[i], yValues[i], widthValues[i], heightValues[i], distanceValues[i], orientationValues[i]));
        }
        VisionData visionData = new VisionData(tagDataBuilder.build());
        currentNavData.setVisionData(visionData);
    }

    private void processNavData() {
        currentNavData.setBatteryLevel(getIntValue(buffer, currentOffset + 4, 4));
        currentNavData.setPitch(getFloatValue(buffer, currentOffset + 8, 4) / 1000);
        currentNavData.setRoll(getFloatValue(buffer, currentOffset + 12, 4) / 1000);
        currentNavData.setYaw(getFloatValue(buffer, currentOffset + 16, 4) / 1000);
        currentNavData.setAltitude((float) getIntValue(buffer, currentOffset + 20, 4) / 1000.0f);
        currentNavData.setSpeedX(getFloatValue(buffer, currentOffset + 24, 4));
        currentNavData.setSpeedY(getFloatValue(buffer, currentOffset + 28, 4));
        currentNavData.setSpeedZ(getFloatValue(buffer, currentOffset + 32, 4));
    }

    private void processCheckSum(int checkSumLength) {
        int checksum = calculateCheckSumForReceivedBytes(checkSumLength);
        int checkSumReceived = getIntValue(buffer, currentOffset, 4);

        checkState(checksum == checkSumReceived, "Wrong checksum calculated");
    }

    private int calculateCheckSumForReceivedBytes(int checkSumLength) {
        int checksum = 0;
        for (int index = 0; index < bufferLength - checkSumLength; index++) {
            checksum += getUnsignedByteValue(buffer[index]);
        }
        return checksum;
    }
}