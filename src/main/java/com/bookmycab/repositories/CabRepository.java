package com.bookmycab.repositories;

import com.bookmycab.CabSnapshot;

import java.util.Collection;

public interface CabRepository {
    void addOrReplaceCab(CabSnapshot cab);

    CabSnapshot getCab(String cabId);

    Collection<CabSnapshot> getAllCabs();
}
