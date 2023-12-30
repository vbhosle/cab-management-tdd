package com.bookmycab;

import com.bookmycab.exception.CabNotAvailableException;

import java.time.Duration;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class CabManager {
    private final Map<String, CabSnapshot> cabs = new HashMap<>();
    private final AppClock clock;

    public CabManager(AppClock clock) {
        this.clock = clock;
    }

    public void registerCab(String cabId, CabState cabState, String cityId) {
        cabs.put(cabId, new CabSnapshot(cabId, cabState, cityId, clock.now()));
    }

    public CabSnapshot getCab(String cabId) {
        return cabs.get(cabId);
    }

    public CabSnapshot getCabForBooking(BookingCriteria bookingCriteria) {
        return cabs.values().stream()
                .filter(cabSnapshot -> cabSnapshot.getState() == CabState.IDLE)
                .filter(cabSnapshot -> cabSnapshot.getCity().equals(bookingCriteria.getCity()))
                .max((cab1, cab2) -> getCabIdleTime(cab1.getId()).compareTo(getCabIdleTime(cab2.getId())))
                .orElseThrow(CabNotAvailableException::new);
    }

    public void changeCurrentCityOfCab(String cabId, String currentCity) {
        CabSnapshot cabSnapshot = cabs.get(cabId);
        cabs.put(cabId, cabSnapshot.withCurrentCity(currentCity));
    }

    public void updateCabToOnTrip(String cabId) {
        CabSnapshot cabSnapshot = cabs.get(cabId);
        cabs.put(cabId, cabSnapshot.onTrip(clock.now()));
    }

    public void updateCabToIdle(String cabId, String currentCityId) {
        CabSnapshot cabSnapshot = cabs.get(cabId);
        cabs.put(cabId, cabSnapshot.toIdle(currentCityId, clock.now()));
    }

    public Duration getCabIdleTime(String cabId) {
        if(cabs.get(cabId).getState() == CabState.IDLE)
            return Duration.between(cabs.get(cabId).getStateChangedAt(), clock.now());
        return Duration.ZERO;
    }
}
