package com.bookmycab;

import com.bookmycab.history.CabAuditor;
import com.bookmycab.history.CabSnapshot;
import com.bookmycab.time.Clock;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class CabManager {
    private LinkedList<String> idleCabList = new LinkedList<>();
    private Map<String, LinkedList<String>> cityToIdleCabList = new HashMap<>();
    private LinkedList<String> onTripCabList = new LinkedList<>();
    private Map<String, LinkedList<String>> cityToOnTripCabList = new HashMap<>();

    private final Map<String, LocalDateTime> cabLastIdleTime = new HashMap<>();

    private final Clock clock;

    private final CabAuditor cabAuditor;

    private final Set<String> onboardedCities = new HashSet<>();

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

    public String book(String city) {
        if(!this.onboardedCities.contains(city))
            throw new ServiceUnavailableInTheCityException();
        if(cityToIdleCabList.get(city).isEmpty())
            throw new CabsNotAvailableException();
        String cabId = cityToIdleCabList.get(city).removeFirst();
        cityToIdleCabList.get(city).remove(cabId);
        cityToOnTripCabList.get(city).add(cabId);
        return cabId;
    }

    public void onboardCity(String city) {
        this.cityToIdleCabList.put(city, new LinkedList<>());
        this.cityToOnTripCabList.put(city, new LinkedList<>());
        this.onboardedCities.add(city);
    }

    public void register(String cityId, String cabId) {
        LocalDateTime now = clock.now();
        cabLastIdleTime.put(cabId, now);
        this.cityToIdleCabList.get(cityId).add(cabId);
        cabAuditor.record(new CabSnapshot(cabId, ""), now);
    }
}
