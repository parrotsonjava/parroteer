package com.dronecontrol.intelcontrol.injection;

import com.dronecontrol.droneapi.DroneController;
import com.dronecontrol.droneapi.ParrotDroneController;
import com.google.inject.Provider;

public class DroneControllerProvider implements Provider<DroneController> {
    public DroneController get() {
        return ParrotDroneController.build();
    }
}