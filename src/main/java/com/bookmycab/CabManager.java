package com.bookmycab;

import com.bookmycab.exception.CabNotAvailableException;
import com.bookmycab.repositories.CabRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CabManager {
    private final AppClock clock;

    private final CabRepository cabRepository;

    public CabManager(CabRepository cabRepository, AppClock clock) {
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
        Predicate<CabSnapshot> filterCriteria = cabSnapshot -> cabSnapshot.getCity().equals(bookingCriteria.getCity());
        Comparator<CabSnapshot> sortingCriteria = Comparator.comparing(CabSnapshot::getStateChangedAt);
        Function<CabSnapshot, Instant> conflictDetectionKey = CabSnapshot::getStateChangedAt;
        List<CabSnapshot> filteredList = cabRepository
                .getAllCabs()
                .stream()
                .filter(cabSnapshot -> cabSnapshot.getState() == CabState.IDLE)
                .filter(filterCriteria)
                .collect(Collectors.toList());
        if (hasConflict(filteredList, conflictDetectionKey))
            Collections.shuffle(filteredList);
        return filteredList
                .stream()
                .min(sortingCriteria)
                .orElseThrow(CabNotAvailableException::new);
    }

    private static <T extends Comparable<T>> boolean hasConflict(List<CabSnapshot> filteredList, Function<CabSnapshot, T> conflictResolutionCriteria) {
        return filteredList.stream().map(conflictResolutionCriteria).distinct().count() == 1;
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
