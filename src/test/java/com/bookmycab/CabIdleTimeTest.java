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
        doReturn(LocalDateTime.of(FIXED_DATE, LocalTime.of(1, 0)))
                .when(mockClock).now();
        CabManager cabManager = new CabManager(new CabAuditor(), mockClock);
        cabManager.register("cab-1");

        doReturn(LocalDateTime.of(FIXED_DATE, LocalTime.of(1, 1)))
                .when(mockClock).now();
        assertThat(cabManager.getIdleTime("cab-1"), equalTo(Duration.ofMinutes(1)));
    }

    @Test
    public void cabIdleTimeIncreasePostRegistrationAsTimePasses() {
        Clock mockClock = Mockito.mock(Clock.class);
        final LocalDate FIXED_DATE = LocalDate.of(2019, 12, 31);
        doReturn(LocalDateTime.of(FIXED_DATE, LocalTime.of(1, 0)))
                .when(mockClock).now();
        CabManager cabManager = new CabManager(new CabAuditor(), mockClock);
        cabManager.register("cab-1");

        doReturn(LocalDateTime.of(FIXED_DATE, LocalTime.of(1, 1)))
                .when(mockClock).now();
        assertThat(cabManager.getIdleTime("cab-1"), equalTo(Duration.ofMinutes(1)));

        doReturn(LocalDateTime.of(FIXED_DATE, LocalTime.of(1, 2)))
                .when(mockClock).now();
        assertThat(cabManager.getIdleTime("cab-1"), equalTo(Duration.ofMinutes(2)));
    }
}
