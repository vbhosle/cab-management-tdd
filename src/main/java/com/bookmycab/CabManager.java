package com.bookmycab;

import com.bookmycab.events.CabEvent;
import com.bookmycab.exception.CabNotAvailableException;
import com.bookmycab.exception.CityNotOnboardedException;
import com.bookmycab.repositories.CabRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CabManager extends Observable{
    private final AppClock clock;

    private final CabRepository cabRepository;

    private final Set<String> onboardedCities = new HashSet<>();

    public CabManager(CabRepository cabRepository, AppClock clock) {
        this.cabRepository = cabRepository;
        this.clock = clock;
    }

    public void registerCab(String cabId, CabState cabState, String cityId) {
        Instant now = clock.now();
        CabSnapshot cabSnapshot = new CabSnapshot(cabId, cabState, cityId, now);
        addOrReplaceCab(cabSnapshot);
    }

    private void addOrReplaceCab(CabSnapshot cabSnapshot) {
        cabRepository.addOrReplaceCab(cabSnapshot);
        setChanged();
        notifyObservers(CabEvent.from(cabSnapshot));
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

    private static <T extends Comparable<T>> boolean hasConflict(List<CabSnapshot> filteredList, Function<CabSnapshot, T> conflictDetectionKey) {
        return filteredList.stream().map(conflictDetectionKey).distinct().count() == 1;
    }

    public void changeCurrentCityOfCab(String cabId, String currentCity) {
        CabSnapshot cabSnapshot = cabRepository.getCab(cabId);
        addOrReplaceCab(cabSnapshot.withCurrentCity(currentCity));
    }

    public CabSnapshot book(String city) {
        if(!isCityOnboarded(city))
            throw new CityNotOnboardedException();
        CabSnapshot availableCab = getCabForBooking(new BookingCriteria(city));
        updateCabToOnTrip(availableCab.getId());
        return availableCab;
    }

    boolean isCityOnboarded(String city) {
        return onboardedCities.contains(city);
    }

    public void onboardCity(String city) {
        onboardedCities.add(city);
    }

    public void updateCabToOnTrip(String cabId) {
        Instant now = clock.now();
        CabSnapshot cabSnapshot = cabRepository.getCab(cabId);
        CabSnapshot newCabSnapshot = cabSnapshot.onTrip(now);
        addOrReplaceCab(newCabSnapshot);
    }

    public void updateCabToIdle(String cabId, String currentCityId) {
        CabSnapshot cabSnapshot = cabRepository.getCab(cabId);
        if(cabSnapshot.getState() == CabState.IDLE && cabSnapshot.getCity().equals(currentCityId))
            return;
        Instant now = clock.now();
        CabSnapshot newCabSnapshot = cabSnapshot.toIdle(currentCityId, now);
        addOrReplaceCab(newCabSnapshot);
    }

    public Duration getCabIdleTime(String cabId) {
        CabSnapshot cab = cabRepository.getCab(cabId);
        if(cab.getState() == CabState.IDLE)
            return Duration.between(cab.getStateChangedAt(), clock.now());
        return Duration.ZERO;
    }
}
