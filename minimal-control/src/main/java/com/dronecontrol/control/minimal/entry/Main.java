package com.dronecontrol.control.minimal.entry;

import com.dronecontrol.droneapi.DroneController;
import com.dronecontrol.droneapi.ParrotDroneController;
import com.dronecontrol.droneapi.data.Config;
import com.dronecontrol.droneapi.listeners.ReadyStateChangeListener;

import java.io.IOException;

public class Main implements ReadyStateChangeListener {
    public static void main(String[] args) throws IOException {
        new Main().start();
    }

    private void start() throws IOException {
        DroneController droneController = ParrotDroneController.build();
        droneController.addReadyStateChangeListener(this);
        droneController.start(new Config("com.dronecontrol.control.minimal", "myProfile", 2));

        System.out.println("Drone startup complete");
        //noinspection ResultOfMethodCallIgnored
        System.in.read();

        droneController.stop();
    }

    @Override
    public void onReadyStateChange(ReadyState readyState) {
        System.out.println("The drone is ready to be used");
    }
}