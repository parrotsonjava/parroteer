package com.dronecontrol.droneapi;

import com.dronecontrol.droneapi.commands.Command;
import com.dronecontrol.droneapi.data.Config;
import com.dronecontrol.droneapi.data.DroneConfiguration;
import com.dronecontrol.droneapi.data.enums.Camera;
import com.dronecontrol.droneapi.data.enums.DroneVersion;
import com.dronecontrol.droneapi.data.enums.FlightAnimation;
import com.dronecontrol.droneapi.data.enums.LedAnimation;
import com.dronecontrol.droneapi.listeners.ErrorListener;
import com.dronecontrol.droneapi.listeners.NavDataListener;
import com.dronecontrol.droneapi.listeners.ReadyStateChangeListener;
import com.dronecontrol.droneapi.listeners.VideoDataListener;

import java.util.concurrent.Future;

public interface DroneController {
    void start(final Config config);

    void startAsync(final Config config);

    void stop();

    void addReadyStateChangeListener(ReadyStateChangeListener readyStateChangeListener);

    void removeReadyStateChangeListener(ReadyStateChangeListener readyStateChangeListener);

    void addErrorListener(ErrorListener errorListener);

    void removeErrorListener(ErrorListener errorListener);

    void addNavDataListener(NavDataListener navDataListener);

    void removeNavDataListener(NavDataListener navDataListener);

    void addVideoDataListener(VideoDataListener videoDataListener);

    void removeVideoDataListener(VideoDataListener videoDataListener);

    DroneVersion getDroneVersion();

    DroneConfiguration getDroneConfiguration();

    void takeOff();

    void land();

    void emergency();

    void flatTrim();

    void move(float roll, float pitch, float yaw, float gaz);

    Future switchCamera(Camera camera);

    Future playLedAnimation(LedAnimation ledAnimation, float frequency, int durationSeconds);

    Future playFlightAnimation(FlightAnimation animation);

    Future setConfigValue(String key, Object value);

    void executeCommands(Command... commands);

    Future executeCommandsAsync(final Command... commands);
}