package com.bookmycab;

import com.bookmycab.exception.CityNotOnboardedException;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

public class InterCityCabBookingTest {

    @Test
    public void bookCabWhenNoCityInSystem() {
        SystemDriver systemDriver = new SystemDriver();

        assertThrows(CityNotOnboardedException.class, () -> systemDriver.book("any-city"));
    }
}
