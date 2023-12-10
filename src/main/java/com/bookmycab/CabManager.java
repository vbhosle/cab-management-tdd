package com.bookmycab;

import com.bookmycab.history.CabAuditor;
import com.bookmycab.history.CabSnapshot;
import com.bookmycab.time.Clock;

import java.util.LinkedList;

public class CabManager {
    private LinkedList<String> idleCabList = new LinkedList<>();
    private LinkedList<String> onTripCabList = new LinkedList<>();

    private final Clock clock;

    private final CabAuditor cabAuditor;

    public CabManager() {
        cabAuditor = new CabAuditor();
        clock = new Clock();
    }

    public CabManager(CabAuditor cabAuditor, Clock clock) {
        this.cabAuditor = cabAuditor;
        this.clock = clock;
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
        cabAuditor.record(new CabSnapshot(cabId, ""), clock.now());
    }

    public void endTrip(String cabId) {
        onTripCabList.remove(cabId);
        idleCabList.add(cabId);
    }

    public void updateLocation(String cabId, String newLocation) {
        cabAuditor.record(new CabSnapshot(cabId, newLocation), null);
    }
}
