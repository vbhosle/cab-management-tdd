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
    public void oneCabRegisteredThenThatCabIsBooked() {
        CabManager cabManager = new CabManager();
        cabManager.register("cab-1");
        assertEquals("cab-1", cabManager.book());
    }

    @Test
    public void oneCabRegisteredThenTwoBookingsAreRequestedOneSucceedsOtherFails() {
        CabManager cabManager = new CabManager();
        cabManager.register("cab-1");
        assertEquals("cab-1", cabManager.book());
        assertThrows(CabsNotAvailableException.class, cabManager::book);
    }

    @Test
    public void inOneCabSystemBookACabEndTripBookAgain() {
        CabManager cabManager = new CabManager();
        cabManager.register("cab-1");
        String booking = cabManager.book();
        assertEquals("cab-1", booking);
        assertNotNull(booking);
        cabManager.endTrip(booking);
        assertEquals("cab-1", cabManager.book());
    }

    @Test
    public void withTwoRegisteredCabsTwoBookingsSucceed() {
        CabManager cabManager = new CabManager();
        cabManager.register("cab-1");
        cabManager.register("cab-2");
        assertNotNull(cabManager.book());
        assertNotNull(cabManager.book());
    }
}
