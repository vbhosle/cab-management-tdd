package com.bookmycab;

public class Booking {
    private String cityId;
    private String cabId;

    public Booking(String cityId, String cabId) {
        this.cityId = cityId;
        this.cabId = cabId;
    }

    public String getCityId() {
        return cityId;
    }

    public String getCabId() {
        return cabId;
    }

}
