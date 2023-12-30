package com.bookmycab;

import com.bookmycab.exception.CabNotAvailableException;

import java.time.Duration;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CabManager {
    private final Map<String, CabSnapshot> cabs = new HashMap<>();
    private final AppClock clock;

    public CabManager(AppClock clock) {
        this.clock = clock;
    }

    public void registerCab(String cabId, CabState cabState, String cityId) {
        addOrReplaceCab(new CabSnapshot(cabId, cabState, cityId, clock.now()));
    }

    private void addOrReplaceCab(CabSnapshot cab) {
        cabs.put(cab.getId(), cab);
    }

    public CabSnapshot getCab(String cabId) {
        return cabs.get(cabId);
    }

    public CabSnapshot getCabForBooking(BookingCriteria bookingCriteria) {
        return findIdleCabs(cabSnapshot -> cabSnapshot.getCity().equals(bookingCriteria.getCity()),
                Comparator.comparing(CabSnapshot::getStateChangedAt))
                .stream()
                .findFirst()
                .orElseThrow(CabNotAvailableException::new);
    }

    public List<CabSnapshot> findIdleCabs(Predicate<CabSnapshot> filterCriteria, Comparator<CabSnapshot> sortingCriteria) {
        return getAllCabs().stream()
            .filter(cabSnapshot -> cabSnapshot.getState() == CabState.IDLE)
            .filter(filterCriteria)
            .sorted(sortingCriteria)
            .collect(Collectors.toList());
    }

    private Collection<CabSnapshot> getAllCabs() {
        return cabs.values();
    }

    public void changeCurrentCityOfCab(String cabId, String currentCity) {
        CabSnapshot cabSnapshot = getCab(cabId);
        addOrReplaceCab(cabSnapshot.withCurrentCity(currentCity));
    }

    public void updateCabToOnTrip(String cabId) {
        CabSnapshot cabSnapshot = getCab(cabId);
        addOrReplaceCab(cabSnapshot.onTrip(clock.now()));
    }

    public void updateCabToIdle(String cabId, String currentCityId) {
        CabSnapshot cabSnapshot = getCab(cabId);
        addOrReplaceCab(cabSnapshot.toIdle(currentCityId, clock.now()));
    }

    public Duration getCabIdleTime(String cabId) {
        if(getCab(cabId).getState() == CabState.IDLE)
            return Duration.between(getCab(cabId).getStateChangedAt(), clock.now());
        return Duration.ZERO;
    }
}
