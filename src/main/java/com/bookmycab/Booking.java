package com.bookmycab;

public class Booking {
    private final CabSnapshot cabSnapshot;
    private final long bookedAt;

    public Booking(CabSnapshot cabSnapshot) {
        this.cabSnapshot = cabSnapshot;
        this.bookedAt = cabSnapshot.getStateChangedAt().toEpochMilli();
    }

    public CabSnapshot getCabSnapshot() {
        return this.cabSnapshot;
    }

    public long getBookedAt() {
        return this.bookedAt;
    }
}
