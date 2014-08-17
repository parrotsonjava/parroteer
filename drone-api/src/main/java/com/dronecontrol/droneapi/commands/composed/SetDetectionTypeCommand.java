package com.dronecontrol.droneapi.commands.composed;

import com.dronecontrol.droneapi.data.DroneConfiguration;
import com.dronecontrol.droneapi.data.LoginData;
import com.dronecontrol.droneapi.data.enums.DetectionType;

public class SetDetectionTypeCommand extends SetConfigValueCommand {
    protected SetDetectionTypeCommand(LoginData loginData, DetectionType detectionType) {
        super(loginData, DroneConfiguration.DETECTION_TYPE_KEY, detectionType.getValue());
    }
}