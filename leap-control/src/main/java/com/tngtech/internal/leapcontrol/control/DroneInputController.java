package com.tngtech.internal.leapcontrol.control;

import com.google.inject.Inject;
import com.tngtech.internal.droneapi.DroneController;
import com.tngtech.internal.droneapi.data.NavData;
import com.tngtech.internal.droneapi.data.enums.Camera;
import com.tngtech.internal.droneapi.data.enums.FlightAnimation;
import com.tngtech.internal.droneapi.data.enums.LedAnimation;
import com.tngtech.internal.droneapi.listeners.NavDataListener;
import com.tngtech.internal.leapcontrol.input.leapmotion.data.DetectionData;
import com.tngtech.internal.leapcontrol.input.leapmotion.data.GestureData;
import com.tngtech.internal.leapcontrol.input.leapmotion.listeners.DetectionListener;
import com.tngtech.internal.leapcontrol.input.leapmotion.listeners.GestureListener;
import com.tngtech.internal.leapcontrol.input.speech.data.SpeechData;
import com.tngtech.internal.leapcontrol.input.speech.listeners.SpeechListener;
import com.tngtech.internal.leapcontrol.ui.data.UIAction;
import com.tngtech.internal.leapcontrol.ui.listeners.UIActionListener;
import org.apache.log4j.Logger;


public class DroneInputController implements NavDataListener, DetectionListener, GestureListener, SpeechListener, UIActionListener
{
  private static final float PITCH_DECAY = 0.5f;

  private static final float ROLL_DECAY = 0.5f;

  // Max height in meters
  private static final float MAX_HEIGHT = 2.0f;

  private static final float HEIGHT_THRESHOLD = 0.25f;

  private final Logger logger = Logger.getLogger(DroneInputController.class);

  private final DroneController droneController;

  private boolean navDataReceived = false;

  private float currentHeight;

  private boolean flying = false;

  private boolean expertMode = false;

  @Inject
  public DroneInputController(DroneController droneController)
  {
    this.droneController = droneController;
  }

  @Override
  public void onSpeech(SpeechData speechData)
  {
    String sentence = speechData.getSentence();
    if (sentence.endsWith("take off"))
    {
      takeOff();
    } else if (sentence.endsWith("land"))
    {
      land();
    } else if (sentence.endsWith("emergency"))
    {
      droneController.emergency();
    } else if (sentence.endsWith("flat trim"))
    {
      droneController.flatTrim();
    }
  }

  @Override
  public void onAction(UIAction action)
  {
    switch (action)
    {
      case TAKE_OFF:
        takeOff();
        break;
      case LAND:
        land();
        break;
      case FLAT_TRIM:
        droneController.flatTrim();
        break;
      case EMERGENCY:
        droneController.emergency();
        break;
      case SWITCH_CAMERA:
        droneController.switchCamera(Camera.NEXT);
        break;
      case PLAY_LED_ANIMATION:
        droneController.playLedAnimation(LedAnimation.RED_SNAKE, 2.0f, 3);
        break;
      case PLAY_FLIGHT_ANIMATION:
        droneController.playFlightAnimation(FlightAnimation.FLIP_LEFT);
        break;
      case ENABLE_EXPERT_MODE:
        logger.warn("Enabling expert mode");
        expertMode = true;
        break;
      case DISABLE_EXPERT_MODE:
        logger.info("Disabling expert mode");
        expertMode = false;
        break;
    }
  }

  @Override
  public void onDetect(DetectionData data)
  {
    float desiredHeight = data.getHeight() * MAX_HEIGHT;
    float heightDelta = navDataReceived ? calculateHeightDelta(desiredHeight)
            : 0.0f;

    if (desiredHeight <= HEIGHT_THRESHOLD)
    {
      land();
    }

    droneController.move(PITCH_DECAY * -data.getRoll(), ROLL_DECAY * data.getPitch(), expertMode ? data.getYaw() : 0.0f, heightDelta);
  }

  @Override
  public void onNoDetect()
  {
    droneController.move(0.0f, 0.0f, 0.0f, 0.0f);
  }

  private float calculateHeightDelta(float desiredHeight)
  {
    return 3 * (desiredHeight - currentHeight) / MAX_HEIGHT;
  }

  @Override
  public void onNavData(NavData navData)
  {
    navDataReceived = true;
    currentHeight = navData.getAltitude() < HEIGHT_THRESHOLD ? 0.0f
            : navData.getAltitude();
  }

  @Override
  public void onGesture(GestureData gestureData)
  {
    switch (gestureData.getGestureType())
    {
      case TYPE_CIRCLE:
        takeOff();
        break;
      case TYPE_SWIPE:
        // TODO Now we could do the flip animation
        break;
    }
  }

  private void takeOff()
  {
    if (!flying)
    {
      flying = true;
      droneController.takeOff();
    }
  }

  private void land()
  {
    if (flying)
    {
      flying = false;
      droneController.land();
    }
  }
}