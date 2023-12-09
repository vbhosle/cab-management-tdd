package com.bookmycab;

public class CabManager {
    public int availableCabs = 0;

    public Object book() {
        if(availableCabs == 0)
            throw new CabsNotAvailableException();
        availableCabs--;
        return new Object();
    }

    public void register(String cabId) {
        availableCabs++;
    }

    public void endTrip(Object booking) {
        availableCabs++;
    }
}
