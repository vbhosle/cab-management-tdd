package com.bookmycab.analytics;

import com.bookmycab.AppClock;
import com.bookmycab.SystemDriver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.bookmycab.models.CabState.IDLE;
import static com.bookmycab.models.CabState.ON_TRIP;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.mockito.Mockito.doReturn;

public class CabIdleTimeAggregationTest {
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
    public void onTripCabIdleTimeDuringPastAndFutureDurationIsZero() {
        systemDriver.addCab("cab-1", ON_TRIP, "city-1");
        progressTimeBy(1, ChronoUnit.HOURS);

        assertThat(systemDriver.getCabIdleTimeBetween("cab-1", now.minus(1, ChronoUnit.HOURS), now), equalTo(Duration.ZERO));
        assertThat(systemDriver.getCabIdleTimeBetween("cab-1", now, now.plus(1, ChronoUnit.HOURS)), equalTo(Duration.ZERO));
    }

    @Test
    public void cabIdleForWholeDay() {
        systemDriver.addCab("cab-1", IDLE, "city-1");
        progressTimeBy(1, ChronoUnit.DAYS);
        assertThat(systemDriver.getCabIdleTimeBetween("cab-1", now.minus(1, ChronoUnit.DAYS), now), equalTo(Duration.of(24, ChronoUnit.HOURS)));
        assertThat(systemDriver.getCabIdleTimeBetween("cab-1", now.minus(2, ChronoUnit.DAYS), now.minus(1, ChronoUnit.DAYS)), equalTo(Duration.ZERO));
    }

    @Test
    public void cabOnTripForWholeDay() {
        systemDriver.addCab("cab-1", ON_TRIP, "city-1");
        progressTimeBy(1, ChronoUnit.DAYS);
        assertThat(systemDriver.getCabIdleTimeBetween("cab-1", now.minus(1, ChronoUnit.DAYS), now), equalTo(Duration.ZERO));
    }

    @Test
    public void cabSwitchesBetweenIdleAndOnTripFor15MinutesEach() {
        systemDriver.addCab("cab-1", IDLE, "city-1");
        progressTimeBy(15, ChronoUnit.MINUTES);

        systemDriver.updateCabToOnTrip("cab-1");
        progressTimeBy(15, ChronoUnit.MINUTES);

        systemDriver.updateCabToIdle("cab-1", "city-1");
        progressTimeBy(15, ChronoUnit.MINUTES);

        systemDriver.updateCabToOnTrip("cab-1");
        progressTimeBy(15, ChronoUnit.MINUTES);


        assertThat(systemDriver.getCabIdleTimeBetween("cab-1", now.minus(1, ChronoUnit.HOURS), now), lessThanOrEqualTo(Duration.of(30, ChronoUnit.MINUTES)));
    }

    public void progressTimeBy(long amountToAdd, ChronoUnit unit) {
        now = now.plus(amountToAdd, unit);
        doReturn(now).when(clock).now();
    }
}
