package com.bookmycab;

import org.junit.Test;

import static org.junit.Assert.assertThrows;

public class CabBookingTest {

    @Test
    public void cabsNotAvailableExceptionWhenNoRegisteredCabs() {
        assertThrows(CabsNotAvailableException.class, () -> new CabManager().book());
    }
}
