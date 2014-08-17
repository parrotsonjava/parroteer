package com.dronecontrol.droneapi.commands.composed;

import com.dronecontrol.droneapi.data.DroneConfiguration;
import com.dronecontrol.droneapi.data.LoginData;
import com.dronecontrol.droneapi.data.enums.TagOption;

import static com.dronecontrol.droneapi.data.enums.TagOption.getBitMaskFor;

public class SetNavDataOptionsCommand extends SetConfigValueCommand {
    protected SetNavDataOptionsCommand(LoginData loginData, TagOption... optionsToUse) {
        super(loginData, DroneConfiguration.NAVDATA_OPTIONS_KEY, getBitMaskFor(optionsToUse));
    }
}