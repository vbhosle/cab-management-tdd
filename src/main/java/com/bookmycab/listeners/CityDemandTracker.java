package com.bookmycab.listeners;

import com.bookmycab.events.BookingEvent;

import java.util.*;
import java.util.stream.Collectors;

public class CityDemandTracker implements Observer {

    private final Map<String, List<BookingEvent>> cityToBookingEvents = new HashMap<>();

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof BookingEvent) {
            BookingEvent bookingEvent = (BookingEvent) arg;
            cityToBookingEvents.computeIfAbsent(bookingEvent.getBooking().getCityId(), x -> new ArrayList<>()).add(bookingEvent);
        }
    }

    public List<String> getCitiesWithHighestBookings(long from, long to, int limit) {
        Map<String, Integer> cityToBookingCount = new HashMap<>();
        cityToBookingEvents.forEach((city, bookingEvents) -> {
            int count = (int) bookingEvents
                    .stream()
                    .filter(bookingEvent -> bookingEvent.getBookedAt() >= from && bookingEvent.getBookedAt() < to)
                    .count();
            cityToBookingCount.put(city, count);
        });
        return cityToBookingCount
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .limit(limit)
                .collect(Collectors.toList());
    }
}
