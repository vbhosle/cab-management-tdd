package com.bookmycab;

public class CabManager {
    public boolean hasCab = false;

    public Object book() {
        if(!hasCab)
            throw new CabsNotAvailableException();
        hasCab = false;
        return new Object();
    }

    public void register(String cabId) {
        hasCab = true;
    }

    public void endTrip(Object booking) {
        hasCab = true;
    }
}
