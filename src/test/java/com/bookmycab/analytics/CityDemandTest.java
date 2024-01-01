package com.bookmycab.analytics;

import static com.bookmycab.models.CabState.IDLE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doReturn;

import com.bookmycab.AppClock;
import com.bookmycab.SystemDriver;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.bookmycab.models.Booking;
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

    @Test
    public void cityWithOneBookingIsHighDemandCity() {
        systemDriver.addCab("cab-1", IDLE, "city-1");
        systemDriver.addCab("cab-2", IDLE, "city-2");
        progressTimeByAMinute();
        systemDriver.book("city-1");
        progressTimeByAMinute();
        List<String> highDemandCities = systemDriver.getHighDemandCities(now.minus(1, ChronoUnit.DAYS), now, 5);
        assertThat(highDemandCities, hasSize(1));
        assertThat(highDemandCities, contains("city-1"));
    }

    @Test
    public void yesterdayCity3AndCity2WereInHighDemandTodayCity2AndCity3AreInHighDemand() {
        systemDriver.addCab("cab-a", IDLE, "city-1");
        systemDriver.addCab("cab-b", IDLE, "city-1");
        systemDriver.addCab("cab-c", IDLE, "city-1");

        systemDriver.addCab("cab-d", IDLE, "city-2");
        systemDriver.addCab("cab-e", IDLE, "city-2");
        systemDriver.addCab("cab-f", IDLE, "city-2");

        systemDriver.addCab("cab-g", IDLE, "city-3");
        systemDriver.addCab("cab-h", IDLE, "city-3");
        systemDriver.addCab("cab-i", IDLE, "city-3");

        progressTimeByAMinute();
        Booking booking = systemDriver.book("city-1");
        systemDriver.updateCabToIdle(booking.getCabId(), "city-1");

        booking = systemDriver.book("city-2");
        systemDriver.updateCabToIdle(booking.getCabId(), "city-2");
        booking = systemDriver.book("city-2");
        systemDriver.updateCabToIdle(booking.getCabId(), "city-2");

        booking = systemDriver.book("city-3");
        systemDriver.updateCabToIdle(booking.getCabId(), "city-3");
        booking = systemDriver.book("city-3");
        systemDriver.updateCabToIdle(booking.getCabId(), "city-3");
        booking = systemDriver.book("city-3");
        systemDriver.updateCabToIdle(booking.getCabId(), "city-3");

        progressTimeByAMinute();

        progressTimeBy(1, ChronoUnit.DAYS);
        systemDriver.book("city-1");

        systemDriver.book("city-2");
        systemDriver.book("city-2");
        systemDriver.book("city-2");

        systemDriver.book("city-3");
        systemDriver.book("city-3");
        progressTimeByAMinute();

        List<String> highDemandCitiesYesterday = systemDriver.getHighDemandCities(now.minus(2, ChronoUnit.DAYS), now.minus(1, ChronoUnit.DAYS), 2);
        List<String> highDemandCitiesInLast24Hours = systemDriver.getHighDemandCities(now.minus(1, ChronoUnit.DAYS), now, 2);
        // assert high demand cities yesterday
        assertThat(highDemandCitiesYesterday, hasSize(2));
        assertThat(highDemandCitiesYesterday, containsInRelativeOrder("city-3", "city-2"));
        assertThat(highDemandCitiesInLast24Hours, hasSize(2));
        assertThat(highDemandCitiesInLast24Hours, containsInRelativeOrder("city-2", "city-3"));
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
