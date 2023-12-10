package com.bookmycab.history;

import com.bookmycab.CabAuditRecord;

import java.time.LocalDateTime;
import java.util.*;

public class CabAuditor {
    private Map<String, List<CabAuditRecord>> cabAuditHistory = new HashMap<>();

    public List<CabAuditRecord> get(String cabId) {
        return cabAuditHistory.getOrDefault(cabId, Collections.emptyList());
    }

    public void record(CabSnapshot cabSnapshot, LocalDateTime createdAt) {
        if (!cabAuditHistory.containsKey(cabSnapshot.getCabId())) {
            cabAuditHistory.put(cabSnapshot.getCabId(), new ArrayList<>());
        }
        cabAuditHistory.get(cabSnapshot.getCabId()).add(new CabAuditRecord(cabSnapshot, createdAt));
    }
}
