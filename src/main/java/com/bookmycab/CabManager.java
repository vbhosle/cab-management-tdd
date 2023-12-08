package com.bookmycab;

public class CabManager {
    public boolean hasCab = false;

    public Object book() {
        if(!hasCab)
            throw new CabsNotAvailableException();
        return new Object();
    }

    public void register(String cabId) {
        hasCab = true;
    }
}
