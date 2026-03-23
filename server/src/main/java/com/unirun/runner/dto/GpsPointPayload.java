package com.unirun.runner.dto;

import jakarta.validation.constraints.NotNull;

public class GpsPointPayload {

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    private Double speed;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }
}
