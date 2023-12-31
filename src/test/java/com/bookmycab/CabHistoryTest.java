package com.bookmycab;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class CabHistoryTest {
    @Test
    public void testAddIdleCabToSystem() {
        SystemDriver systemDriver = new SystemDriver(new AppClock());
        systemDriver.addCab("cab-1", CabState.IDLE, "city-1");
        assertThat(systemDriver.getCabEvents("cab-1"), hasSize(1));
        assertThat(systemDriver.getCabEvents("cab-1").get(0).getState(), equalTo(CabState.IDLE));
    }
}
