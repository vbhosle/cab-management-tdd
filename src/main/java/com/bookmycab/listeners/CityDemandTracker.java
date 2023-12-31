package com.bookmycab.listeners;

import com.bookmycab.events.BookingEvent;

import java.util.*;
import java.util.stream.Collectors;

public class CityDemandTracker implements Observer {

    private final Map<String, TreeSet<BookingEvent>> cityToBookingEvents = new HashMap<>();

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof BookingEvent) {
            BookingEvent bookingEvent = (BookingEvent) arg;
            cityToBookingEvents.computeIfAbsent(bookingEvent.getCabSnapshot().getCity(), x -> new TreeSet<>(Comparator.comparingLong(BookingEvent::getBookedAt))).add(bookingEvent);
        }
    }

    public List<String> getCitiesWithHighestBookings(long from, long to, int limit) {
        Map<String, Integer> cityToBookingCount = cityToBookingEvents.entrySet()
                .stream()
                .map(city -> new AbstractMap.SimpleEntry<>(city.getKey(), city.getValue().subSet(new BookingEvent(null, from), new BookingEvent(null, to)).size()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return cityToBookingCount.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
