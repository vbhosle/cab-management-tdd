package com.bookmycab;

import com.bookmycab.exception.CabNotAvailableException;
import com.bookmycab.repositories.InMemoryCabRepository;

import java.time.Duration;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CabManager {
    private final AppClock clock;

    private final InMemoryCabRepository cabRepository;

    public CabManager(InMemoryCabRepository cabRepository, AppClock clock) {
        this.cabRepository = cabRepository;
        this.clock = clock;
    }

    public void registerCab(String cabId, CabState cabState, String cityId) {
        cabRepository.addOrReplaceCab(new CabSnapshot(cabId, cabState, cityId, clock.now()));
    }

    public CabSnapshot getCab(String cabId) {
        return cabRepository.getCab(cabId);
    }

    public CabSnapshot getCabForBooking(BookingCriteria bookingCriteria) {
    return findIdleCabs(
            cabSnapshot -> cabSnapshot.getCity().equals(bookingCriteria.getCity()),
            Comparator.comparing(CabSnapshot::getStateChangedAt))
            .stream()
            .findFirst()
            .orElseThrow(CabNotAvailableException::new);
    }

    public List<CabSnapshot> findIdleCabs(Predicate<CabSnapshot> filterCriteria, Comparator<CabSnapshot> sortingCriteria) {
        return cabRepository
                .getAllCabs()
                .stream()
                .filter(cabSnapshot -> cabSnapshot.getState() == CabState.IDLE)
                .filter(filterCriteria)
                .sorted(sortingCriteria)
                .collect(Collectors.toList());
    }

    public void changeCurrentCityOfCab(String cabId, String currentCity) {
        CabSnapshot cabSnapshot = cabRepository.getCab(cabId);
        cabRepository.addOrReplaceCab(cabSnapshot.withCurrentCity(currentCity));
    }

    public void updateCabToOnTrip(String cabId) {
        CabSnapshot cabSnapshot = cabRepository.getCab(cabId);
        cabRepository.addOrReplaceCab(cabSnapshot.onTrip(clock.now()));
    }

    public void updateCabToIdle(String cabId, String currentCityId) {
        CabSnapshot cabSnapshot = cabRepository.getCab(cabId);
        cabRepository.addOrReplaceCab(cabSnapshot.toIdle(currentCityId, clock.now()));
    }

    public Duration getCabIdleTime(String cabId) {
        CabSnapshot cab = cabRepository.getCab(cabId);
        if(cab.getState() == CabState.IDLE)
            return Duration.between(cab.getStateChangedAt(), clock.now());
        return Duration.ZERO;
    }
}
