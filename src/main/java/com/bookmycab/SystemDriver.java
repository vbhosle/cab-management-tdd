package com.bookmycab;

import com.bookmycab.exception.CabNotAvailableException;
import com.bookmycab.exception.CityNotOnboardedException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SystemDriver {

    private final Set<String> onboardedCities = new HashSet<>();
    private Map<String, Cab> cabs = new HashMap<>();

    public void book(String city) {
        if(!isCityOnboarded(city))
            throw new CityNotOnboardedException();
        throw new CabNotAvailableException();
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
        cabs.put(cabId, new Cab(cabId, cabState, cityId));
    }

    public Cab getCab(String cabId) {
        return cabs.get(cabId);
    }
}
