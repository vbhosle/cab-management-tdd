package com.bookmycab;

import com.bookmycab.exception.CabNotAvailableException;
import com.bookmycab.exception.CityNotOnboardedException;
import org.junit.Ignore;
import org.junit.Test;

import static com.bookmycab.CabState.IDLE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

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

    @Ignore("Not required anymore: Used for generalization")
    @Test
    public void withCity1Onboarded_bookCabAtCity1FailsWithCabNotAvailableException_bookCabAtCity2FailsWithCityNotOnboardedException() {
        SystemDriver systemDriver = new SystemDriver();
        systemDriver.onboardCity("city-1");

        assertThrows(CabNotAvailableException.class, () -> systemDriver.book("city-1"));
        assertThrows(CityNotOnboardedException.class, () -> systemDriver.book("city-2"));
    }

    @Test
    public void inputCabDetailIsPersisted() {
        SystemDriver systemDriver = new SystemDriver();
        systemDriver.addCab("cab-1", IDLE, "city-1");

        assertTrue(systemDriver.isCityOnboarded("city-1"));

        Cab cab = systemDriver.getCab("cab-1");
        assertThat(cab.getState(), is(IDLE));
        assertThat(cab.getCity(), is("city-1"));
    }
}
