package com.bookmycab;

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
    @Test
    public void idleCabIdleTimeTrackingTest() {
        AppClock clock = Mockito.mock(AppClock.class);
        SystemDriver systemDriver = new SystemDriver(clock);

        Instant now = Instant.now();
        doReturn(now).when(clock).now();
        systemDriver.addCab("cab-1", IDLE, "city-1");

        assertThat(systemDriver.getCabIdleTime("cab-1"), equalTo(Duration.ZERO));

        doReturn(now.plus(1, ChronoUnit.MINUTES)).when(clock).now();
        assertThat(systemDriver.getCabIdleTime("cab-1"), equalTo(Duration.of(1, ChronoUnit.MINUTES)));
    }

    @Test
    public void onTripCabIdleTimeTrackingTest() {
        AppClock clock = Mockito.mock(AppClock.class);
        SystemDriver systemDriver = new SystemDriver(clock);

        Instant now = Instant.now();
        doReturn(now).when(clock).now();
        systemDriver.addCab("cab-1", ON_TRIP, "city-1");

        assertThat(systemDriver.getCabIdleTime("cab-1"), equalTo(Duration.ZERO));

        doReturn(now.plus(1, ChronoUnit.MINUTES)).when(clock).now();
        assertThat(systemDriver.getCabIdleTime("cab-1"), equalTo(Duration.ZERO));
    }
}
