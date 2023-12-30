package com.bookmycab;

public class CabSnapshot {
    private final String id;

    private final CabState state;

    private final String city;

    public CabSnapshot(String id, CabState state, String city) {
        this.id = id;
        this.state = state;
        if(state == CabState.ON_TRIP)
            this.city = "INDETERMINATE";
        else
            this.city = city;
    }

    public CabState getState() {
        return this.state;
    }

    public String getCity() {
        return this.city;
    }

    public String getId() {
        return this.id;
    }

    public CabSnapshot withCurrentCity(String currentCity) {
        if(this.state == CabState.ON_TRIP)
            throw new OperationNotAllowedWhileOnTripException("Changing city not allowed");
        return new CabSnapshot(this.id, this.state, currentCity);
    }

    public CabSnapshot onTrip() {
        return new CabSnapshot(this.id, CabState.ON_TRIP, CabConstants.INDETERMINATE);
    }

    public CabSnapshot toIdle(String currentCityId) {
        return new CabSnapshot(this.id, CabState.IDLE, currentCityId);
    }
}
