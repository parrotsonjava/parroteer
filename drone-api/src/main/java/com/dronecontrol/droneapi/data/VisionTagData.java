package com.dronecontrol.droneapi.data;

public class VisionTagData {

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final int distance;
    private final float orientationAngle;

    public VisionTagData(int x, int y, int width, int height, int distance, float orientationAngle) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.distance = distance;
        this.orientationAngle = orientationAngle;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDistance() {
        return distance;
    }

    public float getOrientationAngle() {
        return orientationAngle;
    }
}