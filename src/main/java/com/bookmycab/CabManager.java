package com.bookmycab;

import com.bookmycab.history.CabAuditor;
import com.bookmycab.history.CabSnapshot;

import java.util.LinkedList;

public class CabManager {
    private LinkedList<String> idleCabList = new LinkedList<>();
    private LinkedList<String> onTripCabList = new LinkedList<>();

    private CabAuditor cabAuditor;

    public CabManager() {
        cabAuditor = new CabAuditor();
    }

    public CabManager(CabAuditor cabAuditor) {
        this.cabAuditor = cabAuditor;
    }

    public String book() {
        if(idleCabList.isEmpty())
            throw new CabsNotAvailableException();
        String cabId = idleCabList.removeFirst();
        onTripCabList.add(cabId);
        return cabId;
    }

    public void register(String cabId) {
        idleCabList.add(cabId);
        cabAuditor.record(new CabSnapshot(cabId, ""));
    }

    public void endTrip(String cabId) {
        onTripCabList.remove(cabId);
        idleCabList.add(cabId);
    }

    public void updateLocation(String cabId, String newLocation) {
        cabAuditor.record(new CabSnapshot(cabId, newLocation));
    }
}
