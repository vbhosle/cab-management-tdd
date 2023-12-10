package com.bookmycab;

import com.bookmycab.history.CabAuditor;
import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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
    public void cabRegistrationIsRecordedByCabAuditorAsVersion1WithTimeStamp() {
        CabAuditor cabAuditor = new CabAuditor();
        new CabManager(cabAuditor).register("cab-1");
        List<CabAuditRecord> cabAuditHistory = cabAuditor.get("cab-1");

        assertThat(cabAuditHistory, hasSize(1));

        CabAuditRecord cabAuditRecord = cabAuditHistory.get(0);
        assertThat(cabAuditRecord.getVersion(), is(1L));
        assertThat(cabAuditRecord.getCabSnapshot().getCabId(), is("cab-1"));
        assertThat(cabAuditRecord.getCreatedAt(), equalTo(LocalDateTime.of(2019, 12, 31, 23, 59, 59)));
    }

    @Ignore
    @Test
    public void cabLocationChangeIsRecordedByCabAuditor() {
        CabAuditor cabAuditor = new CabAuditor();
        CabManager cabManager = new CabManager(cabAuditor);
        cabManager.register("cab-1");
        cabManager.updateLocation("cab-1", "newyork");
        List<CabAuditRecord> cabAuditHistory = cabAuditor.get("cab-1");
        assertThat(cabAuditHistory, hasSize(2));
        assertThat(cabAuditHistory.get(0).getCabSnapshot().getCabId(), is("cab-1"));
        assertThat(cabAuditHistory.get(1).getCabSnapshot().getCabId(), is("cab-1"));
        assertThat(cabAuditHistory.get(1).getCabSnapshot().getLocation(), is("newyork"));
    }
}
