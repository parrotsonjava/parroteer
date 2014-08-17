package com.dronecontrol.droneapi.data;

public class VisionTagData {

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final float orientationAngle;

    public VisionTagData(int x, int y, int width, int height, float orientationAngle) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
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

    public float getOrientationAngle() {
        return orientationAngle;
    }
}