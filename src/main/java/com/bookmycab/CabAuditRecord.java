package com.bookmycab;

import com.bookmycab.history.CabSnapshot;

import java.time.LocalDateTime;

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

    public LocalDateTime getCreatedAt() {
        return null;
    }
}
