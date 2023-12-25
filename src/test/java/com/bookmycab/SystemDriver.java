package com.bookmycab;

import com.bookmycab.exception.CabNotAvailableException;
import com.bookmycab.exception.CityNotOnboardedException;

public class SystemDriver {

    private boolean isCityOnboarded = false;

    public void book(String city) {
        if(!isCityOnboarded(city))
            throw new CityNotOnboardedException();
        throw new CabNotAvailableException();
    }

    private boolean isCityOnboarded(String city) {
        return isCityOnboarded;
    }

    public void onboardCity(String city) {
        isCityOnboarded = true;
    }
}
