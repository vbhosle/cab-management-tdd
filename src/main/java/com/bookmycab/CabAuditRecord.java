package com.bookmycab;

import com.bookmycab.history.CabSnapshot;

public class CabAuditRecord {
    private final CabSnapshot cabSnapshot;

    public CabAuditRecord(CabSnapshot cabSnapshot) {
        this.cabSnapshot = cabSnapshot;
    }

    public CabSnapshot getCabSnapshot() {
        return this.cabSnapshot;
    }

    public long getVersion() {
        return 1L;
    }
}
