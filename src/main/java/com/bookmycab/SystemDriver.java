package com.bookmycab;

import com.bookmycab.exception.CityNotOnboardedException;

import java.util.HashSet;
import java.util.Set;

public class SystemDriver {

    private final Set<String> onboardedCities = new HashSet<>();

    private final CabManager cabManager = new CabManager();

    public void book(String city) {
        if(!isCityOnboarded(city))
            throw new CityNotOnboardedException();
        CabSnapshot availableCab = cabManager.getCabForBooking(city);
        updateCabToOnTrip(availableCab.getId());
    }

    boolean isCityOnboarded(String city) {
        return onboardedCities.contains(city);
    }

    public void onboardCity(String city) {
        onboardedCities.add(city);
    }

    public void addCab(String cabId, CabState cabState, String cityId) {
        onboardCity(cityId);
        cabManager.registerCab(cabId, cabState, cityId);
    }

    public CabSnapshot getCab(String cabId) {
        return cabManager.getCab(cabId);
    }

    public void changeCurrentCityOfCab(String cabId, String currentCity) {
        cabManager.changeCurrentCityOfCab(cabId, currentCity);
    }

    public void updateCabToOnTrip(String cabId) {
        cabManager.updateCabToOnTrip(cabId);
    }

    public void updateCabToIdle(String cabId, String currentCityId) {
        cabManager.updateCabToIdle(cabId, currentCityId);
    }
}
