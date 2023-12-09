package com.bookmycab;

import java.util.LinkedList;

public class CabManager {
    private LinkedList<String> idleCabList = new LinkedList<>();
    private LinkedList<String> onTripCabList = new LinkedList<>();

    public String book() {
        if(idleCabList.isEmpty())
            throw new CabsNotAvailableException();
        String cabId = idleCabList.removeFirst();
        onTripCabList.add(cabId);
        return cabId;
    }

    public void register(String cabId) {
        idleCabList.add(cabId);
    }

    public void endTrip(String cabId) {
        onTripCabList.remove(cabId);
        idleCabList.add(cabId);
    }
}
