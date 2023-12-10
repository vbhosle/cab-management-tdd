package com.bookmycab.history;

public class CabSnapshot {
    private final String cabId;

    public CabSnapshot(String cabId, String newLocation) {
        this.cabId = cabId;
    }

    public String getCabId() {
        return this.cabId;
    }

    public String getLocation() {
        return null;
    }
}
