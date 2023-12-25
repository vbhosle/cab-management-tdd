package com.bookmycab;

import com.bookmycab.exception.CabNotAvailableException;
import com.bookmycab.exception.CityNotOnboardedException;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

public class InterCityCabBookingTest {

    @Test
    public void bookCabWhenNoCityInSystem() {
        SystemDriver systemDriver = new SystemDriver();

        assertThrows(CityNotOnboardedException.class, () -> systemDriver.book("any-city"));
    }

    @Test
    public void bookCabWhenNoCabAddedToTheCity() {
        SystemDriver systemDriver = new SystemDriver();
        systemDriver.onboardCity("city-1");

        assertThrows(CabNotAvailableException.class, () -> systemDriver.book("city-1"));
    }
}
