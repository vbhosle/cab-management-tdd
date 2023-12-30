package com.bookmycab;

import com.bookmycab.exception.CabNotAvailableException;
import com.bookmycab.exception.CityNotOnboardedException;
import org.junit.Test;

import static com.bookmycab.CabState.IDLE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

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

    @Test
    public void oneIdleCabsAddedToEachCity() {
        SystemDriver systemDriver = new SystemDriver();
        systemDriver.addCab("cab-1", IDLE, "city-1");
        systemDriver.addCab("cab-2", IDLE, "city-2");

        CabSnapshot cab1Snapshot = systemDriver.getCab("cab-1");
        CabSnapshot cab2Snapshot = systemDriver.getCab("cab-2");

        assertTrue(systemDriver.isCityOnboarded("city-1"));
        assertTrue(systemDriver.isCityOnboarded("city-2"));

        assertThat(cab1Snapshot.getId(), is("cab-1"));
        assertThat(cab1Snapshot.getState(), is(IDLE));
        assertThat(cab1Snapshot.getCity(), is("city-1"));

        assertThat(cab2Snapshot.getId(), is("cab-2"));
        assertThat(cab2Snapshot.getState(), is(IDLE));
        assertThat(cab2Snapshot.getCity(), is("city-2"));
    }

    @Test
    public void changeCurrentCityOfIdleCab() {
        SystemDriver systemDriver = new SystemDriver();
        systemDriver.addCab("cab-1", IDLE, "city-1");

        systemDriver.changeCurrentCityOfCab("cab-1", "city-2");
        CabSnapshot cab1Snapshot2 = systemDriver.getCab("cab-1");

        assertThat(cab1Snapshot2.getId(), is("cab-1"));
        assertThat(cab1Snapshot2.getState(), is(IDLE));
        assertThat(cab1Snapshot2.getCity(), is("city-2"));
    }

    @Test
    public void addOnTripCab() {
        SystemDriver systemDriver = new SystemDriver();
        systemDriver.addCab("cab-1", CabState.ON_TRIP, "city-1");

        CabSnapshot cab1Snapshot = systemDriver.getCab("cab-1");

        assertThat(cab1Snapshot.getId(), is("cab-1"));
        assertThat(cab1Snapshot.getState(), is(CabState.ON_TRIP));
        assertThat(cab1Snapshot.getCity(), is(CabConstants.INDETERMINATE));
    }

    @Test
    public void addIdleCabAndChangeStateToOnTrip() {
        SystemDriver systemDriver = new SystemDriver();
        systemDriver.addCab("cab-1", IDLE, "city-1");

        systemDriver.updateCabToOnTrip("cab-1");
        CabSnapshot cab1Snapshot2 = systemDriver.getCab("cab-1");

        assertThat(cab1Snapshot2.getId(), is("cab-1"));
        assertThat(cab1Snapshot2.getState(), is(CabState.ON_TRIP));
        assertThat(cab1Snapshot2.getCity(), is(CabConstants.INDETERMINATE));
    }
}
