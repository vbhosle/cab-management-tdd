package com.bookmycab;

import com.bookmycab.events.CabEvent;
import com.bookmycab.models.CabState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doReturn;

public class CabHistoryTest {

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
    public void eventPublishedForEachStateChange() {
        Instant instantAtAdd = now;
        systemDriver.addCab("cab-1", CabState.IDLE, "city-1");

        progressTimeByAMinute();
        Instant instantAtOnTrip = now;
        systemDriver.updateCabToOnTrip("cab-1");

        progressTimeByAMinute();
        Instant instantAtIdle = now;
        systemDriver.updateCabToIdle("cab-1", "city-1");

        assertThat(systemDriver.getCabEvents("cab-1"), hasSize(3));

        CabEvent event1 = systemDriver.getCabEvents("cab-1").get(0);
        CabEvent event2 = systemDriver.getCabEvents("cab-1").get(1);
        CabEvent event3 = systemDriver.getCabEvents("cab-1").get(2);

        assertThat(event1.getState(), equalTo(CabState.IDLE));
        assertThat(event1.getCreatedAt(), equalTo(instantAtAdd.toEpochMilli()));

        assertThat(event2.getState(), equalTo(CabState.ON_TRIP));
        assertThat(event2.getCreatedAt(), equalTo(instantAtOnTrip.toEpochMilli()));

        assertThat(event3.getState(), equalTo(CabState.IDLE));
        assertThat(event3.getCreatedAt(), equalTo(instantAtIdle.toEpochMilli()));
    }

    public void progressTimeByAMinute() {
        now = now.plus(1, ChronoUnit.MINUTES);
        doReturn(now).when(clock).now();
    }
}
