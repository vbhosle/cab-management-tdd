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
    public void inputTwoCabsAddedInSystem() {
        SystemDriver systemDriver = new SystemDriver();
        systemDriver.addCab("cab-1", IDLE, "city-1");
        systemDriver.addCab("cab-2", IDLE, "city-2");

        Cab cab1 = systemDriver.getCab("cab-1");
        Cab cab2 = systemDriver.getCab("cab-2");

        assertTrue(systemDriver.isCityOnboarded("city-1"));
        assertTrue(systemDriver.isCityOnboarded("city-1"));

        assertThat(cab1.getId(), is("cab-1"));
        assertThat(cab1.getState(), is(IDLE));
        assertThat(cab1.getCity(), is("city-1"));

        assertThat(cab2.getId(), is("cab-2"));
        assertThat(cab2.getState(), is(IDLE));
        assertThat(cab2.getCity(), is("city-2"));
    }
}
