package com.bookmycab;

import com.bookmycab.exception.CabNotAvailableException;

import java.util.HashMap;
import java.util.Map;

public class CabManager {
    private final Map<String, CabSnapshot> cabs = new HashMap<>();

    public void registerCab(String cabId, CabState cabState, String cityId) {
        cabs.put(cabId, new CabSnapshot(cabId, cabState, cityId));
    }

    public CabSnapshot getCab(String cabId) {
        return cabs.get(cabId);
    }

    public CabSnapshot getCabForBooking(String city) {
        return cabs.values().stream()
                .filter(cabSnapshot -> cabSnapshot.getState() == CabState.IDLE)
                .filter(cabSnapshot -> cabSnapshot.getCity().equals(city))
                .findFirst()
                .orElseThrow(CabNotAvailableException::new);
    }

    public void changeCurrentCityOfCab(String cabId, String currentCity) {
        CabSnapshot cabSnapshot = cabs.get(cabId);
        cabs.put(cabId, cabSnapshot.withCurrentCity(currentCity));
    }

    public void updateCabToOnTrip(String cabId) {
        CabSnapshot cabSnapshot = cabs.get(cabId);
        cabs.put(cabId, cabSnapshot.onTrip());
    }

    public void updateCabToIdle(String cabId, String currentCityId) {
        CabSnapshot cabSnapshot = cabs.get(cabId);
        cabs.put(cabId, cabSnapshot.toIdle(currentCityId));
    }
}
