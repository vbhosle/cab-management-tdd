package com.bookmycab;

import com.bookmycab.history.CabHistory;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

//# Record of Cab history for each cab. It could be states it went through.
//        - for non-existent cab, cab history is empty.
//        - cab registered, cab history contains cab registration
//        - cab location changed, cab history contains cab registration and cab location change.
//        - cab state changed, cab history contains cab registration and cab state change.
public class CabHistoryTest {
    @Test
    public void cabHistoryIsEmptyForNonExistentCab() {
        assertThat(new CabHistory().get("cab-1"), empty());
    }
}
