package com.dronecontrol.socketcontrol.injection;

import com.dronecontrol.droneapi.ParrotDroneController;
import com.google.inject.Provider;
import com.dronecontrol.droneapi.DroneController;

public class DroneControllerProvider implements Provider<DroneController>
{
  public DroneController get()
  {
    return ParrotDroneController.build();
  }
}