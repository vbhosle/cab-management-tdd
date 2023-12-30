package com.bookmycab;

import com.bookmycab.exception.CabNotAvailableException;
import com.bookmycab.exception.CityNotOnboardedException;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

public class CityOnboardingTest {
    @Test
    public void bookCabWhenNoCityInSystem() {
        SystemDriver systemDriver = new SystemDriver(new AppClock());

        assertThrows(CityNotOnboardedException.class, () -> systemDriver.book("any-city"));
    }

    @Test
    public void bookCabWhenNoCabAddedToTheCity() {
        SystemDriver systemDriver = new SystemDriver(new AppClock());
        systemDriver.onboardCity("city-1");

        assertThrows(CabNotAvailableException.class, () -> systemDriver.book("city-1"));
    }
}
