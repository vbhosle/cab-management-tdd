package com.bookmycab;

import com.bookmycab.history.CabAuditor;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;

//# Record of Cab history for each cab. It could be states it went through.
//        - for non-existent cab, cab history is empty.
//        - cab registered, cab history contains cab registration
//        - cab location changed, cab history contains cab registration and cab location change.
//        - cab state changed, cab history contains cab registration and cab state change.
public class CabAuditorTest {
    @Test
    public void cabAuditIsEmptyForNonExistentCab() {
        assertThat(new CabAuditor().get("cab-1"), empty());
    }

    @Test
    public void cabRegistrationIsRecordedByCabAuditor() {
        new CabManager().register("cab-1");
        List<CabAuditRecord> cabAuditHistory = new CabAuditor().get("cab-1");
        assertThat(cabAuditHistory, hasSize(1));
        assertThat(cabAuditHistory.get(0).getCabSnapshot().getCabId(), is("cab-1"));
    }
}
