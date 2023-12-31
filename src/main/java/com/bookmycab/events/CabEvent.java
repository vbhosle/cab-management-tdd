package com.bookmycab.events;

import com.bookmycab.CabSnapshot;
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

    public static CabEvent from(CabSnapshot cabSnapshot) {
        return new CabEvent(cabSnapshot.getId(), cabSnapshot.getState(), cabSnapshot.getStateChangedAt().toEpochMilli());
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
