package com.bookmycab;

import com.bookmycab.exception.CabNotAvailableException;
import com.bookmycab.exception.CityNotOnboardedException;
import com.sun.glass.ui.Clipboard;

import java.util.HashSet;
import java.util.Set;

public class SystemDriver {

    private final Set<String> onboardedCities = new HashSet<>();

    public void book(String city) {
        if(!isCityOnboarded(city))
            throw new CityNotOnboardedException();
        throw new CabNotAvailableException();
    }

    private boolean isCityOnboarded(String city) {
        return onboardedCities.contains(city);
    }

    public void onboardCity(String city) {
        onboardedCities.add(city);
    }
}
