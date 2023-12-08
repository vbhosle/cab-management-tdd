package com.bookmycab;

public class CabManager {
    public Object book() {
        throw new CabsNotAvailableException();
    }
}
