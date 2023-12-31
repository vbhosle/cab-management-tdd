package com.bookmycab.events;

import com.bookmycab.CabState;

public class CabEvent {
    private final String cabId;
    private final CabState state;

    public CabEvent(String cabId, CabState state) {
        this.cabId = cabId;
        this.state = state;
    }

    public String getCabId() {
        return cabId;
    }

    public CabState getState() {
        return state;
    }
}
