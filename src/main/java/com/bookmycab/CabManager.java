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

public class CabManager {
    private final AppClock clock;

    private final CabRepository cabRepository;

    private final Set<String> onboardedCities = new HashSet<>();

    private final Map<String, List<CabEvent>> cabEvents = new HashMap<>();

    public CabManager(CabRepository cabRepository, AppClock clock) {
        this.cabRepository = cabRepository;
        this.clock = clock;
    }

    public void registerCab(String cabId, CabState cabState, String cityId) {
        Instant now = clock.now();
        cabRepository.addOrReplaceCab(new CabSnapshot(cabId, cabState, cityId, now));
        cabEvents.put(cabId, new ArrayList<>());
        cabEvents.get(cabId).add(new CabEvent(cabId, cabState, now.toEpochMilli()));
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
        cabRepository.addOrReplaceCab(cabSnapshot.withCurrentCity(currentCity));
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
        cabRepository.addOrReplaceCab(newCabSnapshot);
        cabEvents.get(cabId).add(new CabEvent(cabId, newCabSnapshot.getState(), now.toEpochMilli()));
    }

    public void updateCabToIdle(String cabId, String currentCityId) {
        CabSnapshot cabSnapshot = cabRepository.getCab(cabId);
        if(cabSnapshot.getState() == CabState.IDLE && cabSnapshot.getCity().equals(currentCityId))
            return;
        Instant now = clock.now();
        CabSnapshot newCabSnapshot = cabSnapshot.toIdle(currentCityId, now);
        cabRepository.addOrReplaceCab(newCabSnapshot);
        cabEvents.get(cabId).add(new CabEvent(cabId, newCabSnapshot.getState(), now.toEpochMilli()));
    }

    public Duration getCabIdleTime(String cabId) {
        CabSnapshot cab = cabRepository.getCab(cabId);
        if(cab.getState() == CabState.IDLE)
            return Duration.between(cab.getStateChangedAt(), clock.now());
        return Duration.ZERO;
    }

    public List<CabEvent> getCabEvents(String cabId) {
        return cabEvents.get(cabId);
    }

    public Duration getCabIdleTimeBetween(String cabId, Instant from, Instant to) {
        List<CabEvent> events = getCabEvents(cabId)
                .stream()
                .filter(cabEvent -> cabEvent.getCreatedAt() >= from.toEpochMilli())
                .filter(cabEvent -> cabEvent.getCreatedAt() < to.toEpochMilli())
                .collect(Collectors.toList());
        // find first IDLE event. find time difference between that and next ON_TRIP event. do it in a loop
        Duration totalIdleTime = Duration.ZERO;
        for(int i = 0; i < events.size(); i++) {
            CabEvent event = events.get(i);
            if(event.getState() == CabState.IDLE) {
                if(i + 1 < events.size()) {
                    CabEvent nextEvent = events.get(i + 1);
                    if(nextEvent.getState() == CabState.ON_TRIP) {
                        totalIdleTime = totalIdleTime.plus(Duration.between(Instant.ofEpochMilli(event.getCreatedAt()), Instant.ofEpochMilli(nextEvent.getCreatedAt())));
                    }
                }
                else {
                    totalIdleTime = totalIdleTime.plus(Duration.between(Instant.ofEpochMilli(event.getCreatedAt()), clock.now()));
                }
            }
        }

        return totalIdleTime;
    }
}
