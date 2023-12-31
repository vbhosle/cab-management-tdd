package com.bookmycab;

import com.bookmycab.events.CabEvent;
import com.bookmycab.exception.CityNotOnboardedException;
import com.bookmycab.repositories.InMemoryCabRepository;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SystemDriver {

    private final Set<String> onboardedCities = new HashSet<>();

    private final CabManager cabManager;

    public SystemDriver(AppClock clock) {
        cabManager = new CabManager(new InMemoryCabRepository(), clock);
    }

    public CabSnapshot book(String city) {
        if(!isCityOnboarded(city))
            throw new CityNotOnboardedException();
        CabSnapshot availableCab = cabManager.getCabForBooking(new BookingCriteria(city));
        updateCabToOnTrip(availableCab.getId());
        return availableCab;
    }

    boolean isCityOnboarded(String city) {
        return onboardedCities.contains(city);
    }

    public void onboardCity(String city) {
        onboardedCities.add(city);
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
        return cabManager.getCabEvents(cabId);
    }
}
