package com.bookmycab;

import org.junit.Test;

import static org.junit.Assert.*;

public class CabBookingTest {

    @Test
    public void whenNoCityOnboardedThenBookingThrowsServiceUnavailableInTheCityException() {
        assertThrows(ServiceUnavailableInTheCityException.class, () -> new CabManager().book("city-1"));
    }

    @Test
    public void cabsNotAvailableExceptionWhenNoRegisteredCabsInTheCity() {
        CabManager cabManager = new CabManager();
        cabManager.onboardCity("city-1");
        assertThrows(CabsNotAvailableException.class, () -> cabManager.book("city-1"));
    }

    @Test
    public void twoCitiesOnboardedBookingCabInBothCitiesFailsWithCabsNotAvailableExceptionAndFailsInUnknownCityWithServiceNotAvailableInTheCity() {
        CabManager cabManager = new CabManager();
        cabManager.onboardCity("city-1");
        cabManager.onboardCity("city-2");
        assertThrows(CabsNotAvailableException.class, () -> cabManager.book("city-1"));
        assertThrows(CabsNotAvailableException.class, () -> cabManager.book("city-2"));
        assertThrows(ServiceUnavailableInTheCityException.class, () -> cabManager.book("city-3"));
    }

    @Test
    public void oneCabRegisteredThenThatCabIsBooked() {
        CabManager cabManager = new CabManager();
        cabManager.onboardCity("city-1");
        cabManager.register("city-1", "cab-1");
        assertEquals("cab-1", cabManager.book("city-1"));
    }

    @Test
    public void cabIsInCity1BookingFromAnotherCityFails() {
        CabManager cabManager = new CabManager();
        cabManager.onboardCity("city-1");
        cabManager.onboardCity("city-2");
        cabManager.register("city-1", "cab-1");
        assertThrows(CabsNotAvailableException.class, () -> cabManager.book("city-2"));
    }

    @Test
    public void oneCabRegisteredThenTwoBookingsAreRequestedOneSucceedsOtherFails() {
        CabManager cabManager = new CabManager();
        cabManager.onboardCity("city-1");
        cabManager.register("city-1", "cab-1");
        assertEquals("cab-1", cabManager.book("city-1"));
        assertThrows(CabsNotAvailableException.class, () -> cabManager.book("city-1"));
    }

    @Test
    public void inOneCabSystemBookACabEndTripBookAgain() {
        CabManager cabManager = new CabManager();
        cabManager.onboardCity("city-1");
        cabManager.register("city-1", "cab-1");
        String booking = cabManager.book("city-1");
        assertEquals("cab-1", booking);
        assertNotNull(booking);
        cabManager.endTrip(booking);
        assertEquals("cab-1", cabManager.book("city-1"));
    }

    @Test
    public void withTwoRegisteredCabsTwoBookingsSucceed() {
        CabManager cabManager = new CabManager();
        cabManager.onboardCity("city-1");
        cabManager.register("city-1", "cab-1");
        cabManager.register("city-1", "cab-2");
        assertNotNull(cabManager.book("city-1"));
        assertNotNull(cabManager.book("city-1"));
    }
}
