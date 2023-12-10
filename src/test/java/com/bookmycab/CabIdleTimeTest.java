package com.bookmycab;

import org.junit.Test;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class CabIdleTimeTest {

    @Test
    public void cabIdleFor1MinutePostRegistration() {
        CabManager cabManager = new CabManager();
        cabManager.register("cab-1");
        assertThat(cabManager.getIdleTime("cab-1"), equalTo(Duration.ofMinutes(1)));
    }
}
