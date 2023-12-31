package com.bookmycab.models;

import com.bookmycab.CabConstants;
import com.bookmycab.exception.OperationNotAllowedWhileOnTripException;

import java.time.Instant;

public class CabSnapshot {
    private final String id;

    private final CabState state;

    private final String city;

    private final Instant stateChangedAt;

    public CabSnapshot(String id, CabState state, String city, Instant stateChangedAt) {
        this.id = id;
        this.state = state;
        if(state == CabState.ON_TRIP)
            this.city = "INDETERMINATE";
        else
            this.city = city;
        this.stateChangedAt = stateChangedAt;
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
        return new CabSnapshot(this.id, this.state, currentCity, this.stateChangedAt);
    }

    public CabSnapshot onTrip(Instant stateChangedAt) {
        return new CabSnapshot(this.id, CabState.ON_TRIP, CabConstants.INDETERMINATE, stateChangedAt);
    }

    public CabSnapshot toIdle(String currentCityId, Instant stateChangedAt) {
        return new CabSnapshot(this.id, CabState.IDLE, currentCityId, stateChangedAt);
    }

    public Instant getStateChangedAt() {
        return stateChangedAt;
    }
}
