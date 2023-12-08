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
}
