package com.bookmycab;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doReturn;

public class TwoCabsSystemTest {
    private SystemDriver systemDriver;
    private AppClock clock;
    private Instant now;

    @Before
    public void setUp() {
        clock = Mockito.mock(AppClock.class);
        systemDriver = new SystemDriver(clock);
        now = Instant.now();
        doReturn(now).when(clock).now();
    }

    @Test
    public void bookMostIdleCabWhenTwoIdleCabsInTheCity() {
        systemDriver.addCab("cab-red", CabState.IDLE, "city-1");
        progressTimeByAMinute();
        systemDriver.addCab("cab-blue", CabState.IDLE, "city-1");

        systemDriver.updateCabToOnTrip("cab-red");
        progressTimeByAMinute();
        systemDriver.updateCabToIdle("cab-red", "city-1");

        assertThat(systemDriver.getCabIdleTime("cab-blue"), equalTo(Duration.of(1, ChronoUnit.MINUTES)));
        assertThat(systemDriver.getCabIdleTime("cab-red"), equalTo(Duration.ZERO));
        systemDriver.book("city-1");

        CabSnapshot cabRedSnapshot = systemDriver.getCab("cab-red");
        CabSnapshot cabBlueSnapshot = systemDriver.getCab("cab-blue");

        assertThat("blue cab ON_TRIP", cabBlueSnapshot.getState(), equalTo(CabState.ON_TRIP));
        assertThat("red cab IDLE", cabRedSnapshot.getState(), equalTo(CabState.IDLE));
    }

    public void progressTimeByAMinute() {
        now = now.plus(1, ChronoUnit.MINUTES);
        doReturn(now).when(clock).now();
    }
}
