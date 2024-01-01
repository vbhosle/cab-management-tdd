package com.bookmycab.models;

public class Booking {

    private final String cabId;
    private final String cityId;
    private final long bookedAt;

    public Booking(String cabId, String cityId, long bookedAt) {
        this.cabId = cabId;
        this.cityId = cityId;
        this.bookedAt = bookedAt;
    }

    public String getCabId() {
        return this.cabId;
    }

    public String getCityId() {
        return this.cityId;
    }

    public long getBookedAt() {
        return this.bookedAt;
    }
}
