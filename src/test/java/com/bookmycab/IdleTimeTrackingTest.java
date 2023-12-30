package com.bookmycab;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.bookmycab.CabState.IDLE;
import static com.bookmycab.CabState.ON_TRIP;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doReturn;

public class IdleTimeTrackingTest {

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
    public void idleCabIdleTimeTrackingTest() {
        systemDriver.addCab("cab-1", IDLE, "city-1");

        assertThat(systemDriver.getCabIdleTime("cab-1"), equalTo(Duration.ZERO));

        progressTimeByAMinute();
        assertThat(systemDriver.getCabIdleTime("cab-1"), equalTo(Duration.of(1, ChronoUnit.MINUTES)));
    }

    @Test
    public void onTripCabIdleTimeTrackingTest() {
        systemDriver.addCab("cab-1", ON_TRIP, "city-1");

        assertThat(systemDriver.getCabIdleTime("cab-1"), equalTo(Duration.ZERO));

        progressTimeByAMinute();
        assertThat(systemDriver.getCabIdleTime("cab-1"), equalTo(Duration.ZERO));
    }

    public void progressTimeByAMinute() {
        doReturn(now.plus(1, ChronoUnit.MINUTES)).when(clock).now();
    }
}
