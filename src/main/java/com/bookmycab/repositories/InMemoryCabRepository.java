package com.bookmycab.repositories;

import com.bookmycab.CabSnapshot;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InMemoryCabRepository implements CabRepository {
  private final Map<String, CabSnapshot> cabs = new HashMap<>();

  public InMemoryCabRepository() {}

  @Override
  public void addOrReplaceCab(CabSnapshot cab) {
    cabs.put(cab.getId(), cab);
  }

  @Override
  public CabSnapshot getCab(String cabId) {
    return cabs.get(cabId);
  }

  @Override
  public Collection<CabSnapshot> getAllCabs() {
    return cabs.values();
  }
}