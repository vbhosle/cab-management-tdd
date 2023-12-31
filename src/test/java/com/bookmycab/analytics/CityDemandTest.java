package com.bookmycab.analytics;

import static com.bookmycab.models.CabState.IDLE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doReturn;

import com.bookmycab.AppClock;
import com.bookmycab.SystemDriver;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class CityDemandTest {
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
    public void noHighDemandCityInEmptySystem() {
        assertThat(systemDriver.getHighDemandCities(now.minus(1, ChronoUnit.DAYS), now, 5), hasSize(0));
    }

    @Test
    public void noHighDemandCityInSystemWithNoBooking() {
        systemDriver.addCab("cab-1", IDLE, "city-1");
        systemDriver.addCab("cab-2", IDLE, "city-2");
        progressTimeByAMinute();
        assertThat(systemDriver.getHighDemandCities(now.minus(1, ChronoUnit.DAYS), now, 5), hasSize(0));
    }

    public void progressTimeByAMinute() {
        now = now.plus(1, ChronoUnit.MINUTES);
        doReturn(now).when(clock).now();
    }

    public void progressTimeBy(long amountToAdd, ChronoUnit unit) {
        now = now.plus(amountToAdd, unit);
        doReturn(now).when(clock).now();
    }
}
