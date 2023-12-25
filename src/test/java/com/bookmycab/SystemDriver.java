package com.bookmycab;

import com.bookmycab.exception.CityNotOnboardedException;

public class SystemDriver {
    public void book(String city) {
        throw new CityNotOnboardedException();
    }
}
