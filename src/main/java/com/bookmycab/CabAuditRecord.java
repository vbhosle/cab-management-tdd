package com.bookmycab;

import com.bookmycab.history.CabSnapshot;

import java.time.LocalDateTime;

public class CabAuditRecord {
    private final CabSnapshot cabSnapshot;
    private final LocalDateTime createdAt;

    public CabAuditRecord(CabSnapshot cabSnapshot, LocalDateTime createdAt) {
        this.cabSnapshot = cabSnapshot;
        this.createdAt = createdAt;
    }

    public CabSnapshot getCabSnapshot() {
        return this.cabSnapshot;
    }

    public long getVersion() {
        return 1L;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }
}
