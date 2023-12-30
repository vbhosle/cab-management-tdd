package com.bookmycab;

import com.bookmycab.exception.CabNotAvailableException;
import com.bookmycab.exception.CityNotOnboardedException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SystemDriver {

    private final Set<String> onboardedCities = new HashSet<>();
    private Map<String, CabSnapshot> cabs = new HashMap<>();

    public void book(String city) {
        if(!isCityOnboarded(city))
            throw new CityNotOnboardedException();
        CabSnapshot availableCab = getCabForBooking(city);
        updateCabToOnTrip(availableCab.getId());
    }

    private CabSnapshot getCabForBooking(String city) {
        return cabs.values().stream()
                .filter(cabSnapshot -> cabSnapshot.getState() == CabState.IDLE)
                .filter(cabSnapshot -> cabSnapshot.getCity().equals(city))
                .findFirst()
                .orElseThrow(CabNotAvailableException::new);
    }

    boolean isCityOnboarded(String city) {
        return onboardedCities.contains(city);
    }

    public void onboardCity(String city) {
        onboardedCities.add(city);
    }

    public void addCab(String cabId, CabState cabState, String cityId) {
        onboardCity(cityId);
        registerCab(cabId, cabState, cityId);
    }

    private void registerCab(String cabId, CabState cabState, String cityId) {
        cabs.put(cabId, new CabSnapshot(cabId, cabState, cityId));
    }

    public CabSnapshot getCab(String cabId) {
        return cabs.get(cabId);
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
