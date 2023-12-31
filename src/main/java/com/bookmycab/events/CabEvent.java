package com.bookmycab.events;

import com.bookmycab.CabState;

public class CabEvent {
    private final String cabId;
    private final CabState state;
    private final long createdAt;

    public CabEvent(String cabId, CabState state, long createdAt) {
        this.cabId = cabId;
        this.state = state;
        this.createdAt = createdAt;
    }

    public String getCabId() {
        return cabId;
    }

    public CabState getState() {
        return state;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
