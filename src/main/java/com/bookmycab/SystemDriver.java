package com.bookmycab;

import com.bookmycab.events.CabEvent;
import com.bookmycab.listeners.CabEventListener;
import com.bookmycab.repositories.InMemoryCabRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class SystemDriver {

    private final CabManager cabManager;
    private final CabEventListener cabEventListener;

    public SystemDriver(AppClock clock) {
        cabManager = new CabManager(new InMemoryCabRepository(), clock);
        cabEventListener = new CabEventListener(clock);
        cabManager.addObserver(cabEventListener);
    }

    public CabSnapshot book(String city) {
        return cabManager.book(city);
    }

    boolean isCityOnboarded(String city) {
        return cabManager.isCityOnboarded(city);
    }

    public void onboardCity(String city) {
        cabManager.onboardCity(city);
    }

    public void addCab(String cabId, CabState cabState, String cityId) {
        onboardCity(cityId);
        cabManager.registerCab(cabId, cabState, cityId);
    }

    public CabSnapshot getCab(String cabId) {
        return cabManager.getCab(cabId);
    }

    public void changeCurrentCityOfCab(String cabId, String currentCity) {
        cabManager.changeCurrentCityOfCab(cabId, currentCity);
    }

    public void updateCabToOnTrip(String cabId) {
        cabManager.updateCabToOnTrip(cabId);
    }

    public void updateCabToIdle(String cabId, String currentCityId) {
        cabManager.updateCabToIdle(cabId, currentCityId);
    }

    public Duration getCabIdleTime(String cabId) {
        return cabManager.getCabIdleTime(cabId);
    }

    public List<CabEvent> getCabEvents(String cabId) {
        return cabEventListener.getCabEvents(cabId);
    }

    public Duration getCabIdleTimeBetween(String cabId, Instant from, Instant to) {
        return cabEventListener.getCabIdleTimeBetween(cabId, from, to);
    }
}
