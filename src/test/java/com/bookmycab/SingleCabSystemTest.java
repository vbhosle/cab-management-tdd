package com.bookmycab;

import com.bookmycab.exception.CabNotAvailableException;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThrows;

public class SingleCabSystemTest {
    private SystemDriver systemDriver;

    @Before
    public void setUp() {
        systemDriver = new SystemDriver();
        systemDriver.onboardCity("city-1");
    }

    @Test
    public void bookCabWhenOneOnTripCabAddedToTheCity() {
        systemDriver.addCab("cab-1", CabState.ON_TRIP, "city-1");
        assertThrows(CabNotAvailableException.class, () -> systemDriver.book("city-1"));
    }

    @Test
    public void bookCabWhenOneIdleCabAddedToTheCity() {
        systemDriver.addCab("cab-1", CabState.IDLE, "city-1");
        systemDriver.book("city-1");
        CabSnapshot cab1Snapshot = systemDriver.getCab("cab-1");
        assertThat(cab1Snapshot.getState(), equalTo(CabState.ON_TRIP));
    }

    @Test
    public void changeIdleCabLocationFromCity1ToCity2BookingForCity1Fails() {
        systemDriver.addCab("cab-1", CabState.IDLE, "city-1");
        systemDriver.changeCurrentCityOfCab("cab-1", "city-2");
        assertThrows(CabNotAvailableException.class, () -> systemDriver.book("city-1"));
    }

    @Test
    public void changeOnTripCabToIdleAtCity1ThenBookingCabForCity1Succeeds() {
        systemDriver.addCab("cab-1", CabState.ON_TRIP, "city-1");
        systemDriver.updateCabToIdle("cab-1", "city-1");
        systemDriver.book("city-1");

        CabSnapshot cab1Snapshot = systemDriver.getCab("cab-1");
        assertThat(cab1Snapshot.getState(), equalTo(CabState.ON_TRIP));
    }

    @Test
    public void changeIdleInCity1ToOnTripThenBookingCabForCity1Fails() {
        systemDriver.addCab("cab-1", CabState.IDLE, "city-1");
        systemDriver.updateCabToOnTrip("cab-1");
        assertThrows(CabNotAvailableException.class, () -> systemDriver.book("city-1"));
    }
}
