package com.bookmycab;

import com.bookmycab.history.CabAuditor;
import com.bookmycab.time.Clock;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.doReturn;

public class CabIdleTimeTest {

    @Test
    public void cabIdleFor1MinutePostRegistration() {
        Clock mockClock = Mockito.mock(Clock.class);
        final LocalDate FIXED_DATE = LocalDate.of(2019, 12, 31);
        final LocalTime FIXED_TIME = LocalTime.of(1, 0);
        final LocalDateTime FIXED_DATE_TIME = LocalDateTime.of(FIXED_DATE, FIXED_TIME);

        doReturn(FIXED_DATE_TIME)
                .when(mockClock).now();
        CabManager cabManager = new CabManager(new CabAuditor(), mockClock);
        cabManager.onboardCity("city-1");
        cabManager.register("city-1", "cab-1");

        doReturn(FIXED_DATE_TIME.plusMinutes(1))
                .when(mockClock).now();
        assertThat(cabManager.getIdleTime("cab-1"), equalTo(Duration.ofMinutes(1)));
    }

    @Test
    public void cabIdleTimeIncreasePostRegistrationAsTimePasses() {
        Clock mockClock = Mockito.mock(Clock.class);
        final LocalDate FIXED_DATE = LocalDate.of(2019, 12, 31);
        final LocalTime FIXED_TIME = LocalTime.of(1, 0);
        final LocalDateTime FIXED_DATE_TIME = LocalDateTime.of(FIXED_DATE, FIXED_TIME);

        doReturn(FIXED_DATE_TIME)
                .when(mockClock).now();
        CabManager cabManager = new CabManager(new CabAuditor(), mockClock);
        cabManager.onboardCity("city-1");
        cabManager.register("city-1", "cab-1");

        doReturn(FIXED_DATE_TIME.plusMinutes(1))
                .when(mockClock).now();
        assertThat(cabManager.getIdleTime("cab-1"), equalTo(Duration.ofMinutes(1)));

        doReturn(FIXED_DATE_TIME.plusMinutes(2))
                .when(mockClock).now();
        assertThat(cabManager.getIdleTime("cab-1"), equalTo(Duration.ofMinutes(2)));
    }
}
