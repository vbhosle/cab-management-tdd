package com.bookmycab;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class CabBookingTest {

    @Test
    public void cabsNotAvailableExceptionWhenNoRegisteredCabs() {
        assertThrows(CabsNotAvailableException.class, () -> new CabManager().book());
    }

    @Test
    public void oneCabRegisteredThenBookingIsReturned() {
        CabManager cabManager = new CabManager();
        cabManager.register("cab-1");
        assertNotNull(cabManager.book());
    }

    @Test
    public void oneCabRegisteredThenTwoBookingsAreRequestedOneSucceedsOtherFails() {
        CabManager cabManager = new CabManager();
        cabManager.register("cab-1");
        assertNotNull(cabManager.book());
        assertThrows(CabsNotAvailableException.class, cabManager::book);
    }

    @Test
    public void inOneCabSystemBookACabEndTripBookAgain() {
        CabManager cabManager = new CabManager();
        cabManager.register("cab-1");
        Object booking = cabManager.book();
        assertNotNull(booking);
        cabManager.endTrip(booking);
        assertNotNull(cabManager.book());
    }
}
