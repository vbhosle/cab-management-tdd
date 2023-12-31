package com.bookmycab.repositories;

import com.bookmycab.models.CabSnapshot;

import java.util.Collection;

public interface CabRepository {
    void addOrReplaceCab(CabSnapshot cab);

    CabSnapshot getCab(String cabId);

    Collection<CabSnapshot> getAllCabs();
}
