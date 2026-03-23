package com.unirun.runner.entity;

public class GpsPoint {
    private double latitude;
    private double longitude;
    private Double speed;

    public GpsPoint() {
    }

    public GpsPoint(double latitude, double longitude, Double speed) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }
}
