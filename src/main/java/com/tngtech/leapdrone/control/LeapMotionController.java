package com.tngtech.leapdrone.control;

import com.leapmotion.leap.Controller;

public class LeapMotionController {
	LeapMotionListener listener;

	Controller controller;

	public LeapMotionController() {
		this.listener = new LeapMotionListener();
		this.controller = new Controller();
	}

	public float getHeight() {
		return listener.getHandHeight();
	}

	public double getHandPitchInDegrees() {
		return listener.getHandPitchInDegrees();
	}

	public double getHandRollInDegrees() {
		return listener.getHandRollInDegrees();
	}

	public void connect() {
		// Have the sample listener receive events from the controller
		controller.addListener(listener);
	}
	
	public void disconnect() {
		// Have the sample listener receive events from the controller
		controller.removeListener(listener);
	}
}