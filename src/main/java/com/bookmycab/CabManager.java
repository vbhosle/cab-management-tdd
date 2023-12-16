package com.bookmycab;

import com.bookmycab.history.CabAuditor;
import com.bookmycab.history.CabSnapshot;
import com.bookmycab.time.Clock;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class CabManager {
    private LinkedList<String> idleCabList = new LinkedList<>();
    private LinkedList<String> onTripCabList = new LinkedList<>();

    private final Map<String, LocalDateTime> cabLastIdleTime = new HashMap<>();

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
        cabLastIdleTime.remove(cabId);
        onTripCabList.add(cabId);
        return cabId;
    }

    public void register(String cabId) {
        LocalDateTime now = clock.now();
        cabLastIdleTime.put(cabId, now);
        idleCabList.add(cabId);
        cabAuditor.record(new CabSnapshot(cabId, ""), now);
    }

    public void endTrip(String cabId) {
        onTripCabList.remove(cabId);
        cabLastIdleTime.put(cabId, clock.now());
        idleCabList.add(cabId);
    }

    public void updateLocation(String cabId, String newLocation) {
        cabAuditor.record(new CabSnapshot(cabId, newLocation), null);
    }

    public Duration getIdleTime(String cabId) {
        return Duration.between(cabLastIdleTime.get(cabId), clock.now());
    }

    public void book(String city) {
        throw new ServiceUnavailableInTheCityException();
    }

    public void onboardCity(String city) {

    }
}
