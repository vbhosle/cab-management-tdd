package com.bookmycab;

import com.bookmycab.history.CabAuditor;
import com.bookmycab.history.CabSnapshot;
import com.bookmycab.time.Clock;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class CabManager {
    private Map<String, LinkedList<String>> cityToIdleCabList = new HashMap<>();
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

    public void endTrip(Booking booking) {
        cabLastIdleTime.put(booking.getCabId(), clock.now());
        this.cityToOnTripCabList.get(booking.getCityId()).remove(booking.getCabId());
        this.cityToIdleCabList.get(booking.getCityId()).add(booking.getCabId());
    }

    public void updateLocation(String cabId, String newLocation) {
        cabAuditor.record(new CabSnapshot(cabId, newLocation), null);
    }

    public Duration getIdleTime(String cabId) {
        return Duration.between(cabLastIdleTime.get(cabId), clock.now());
    }

    public Booking book(String cityId) {
        if(!this.onboardedCities.contains(cityId))
            throw new ServiceUnavailableInTheCityException();
        if(cityToIdleCabList.get(cityId).isEmpty())
            throw new CabsNotAvailableException();
        String cabId = cityToIdleCabList.get(cityId).removeFirst();
        cityToIdleCabList.get(cityId).remove(cabId);
        cityToOnTripCabList.get(cityId).add(cabId);
        return new Booking(cityId, cabId);
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
