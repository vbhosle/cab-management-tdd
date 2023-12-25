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

    /*
    System with city-1 onboarded. Attempt to book a cab at city-1, fails with CabNotAvailableException. Attempt to book a cab in city-2, fails with CityNotOnboardedException.
    Outline:

    onboard city-1
    assert CabNotAvailableException thrown when book cab at city-1
    assert CityNotOnboardedException thrown when book cab at city-2
     */
    @Test
    public void withCity1Onboarded_bookCabAtCity1FailsWithCabNotAvailableException_bookCabAtCity2FailsWithCityNotOnboardedException() {
        SystemDriver systemDriver = new SystemDriver();
        systemDriver.onboardCity("city-1");

        assertThrows(CabNotAvailableException.class, () -> systemDriver.book("city-1"));
        assertThrows(CityNotOnboardedException.class, () -> systemDriver.book("city-2"));
    }
}
