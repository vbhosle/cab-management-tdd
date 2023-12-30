package com.bookmycab;

import org.junit.Test;

import static com.bookmycab.CabState.IDLE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotSame;

public class CabSnapshotTest {
    @Test
    public void changeCurrentCityOfIdleCab() {
        CabSnapshot cabSnapshot1 = new CabSnapshot("cab-1", IDLE, "city-1");
        CabSnapshot cabSnapshot2 = cabSnapshot1.withCurrentCity("city-2");

        assertNotSame(cabSnapshot1, cabSnapshot2);
        assertThat(cabSnapshot2.getId(), is(cabSnapshot1.getCity()));
        assertThat(cabSnapshot2.getState(), is(cabSnapshot1.getState()));
        assertThat(cabSnapshot2.getCity(), is("city-2"));
    }
}
