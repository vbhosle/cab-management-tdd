package com.bookmycab.listeners;

import com.bookmycab.AppClock;
import com.bookmycab.models.CabState;
import com.bookmycab.events.CabEvent;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class CabEventListener implements Observer {
    private final Map<String, List<CabEvent>> cabIdToCabEvents = new HashMap<>();

    private final AppClock clock;

    public CabEventListener(AppClock clock) {
        this.clock = clock;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof CabEvent) {
            CabEvent cabEvent = (CabEvent) arg;
            cabIdToCabEvents.computeIfAbsent(cabEvent.getCabId(), x -> new ArrayList<>()).add(cabEvent);
        }
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

    public List<CabEvent> getCabEvents(String cabId) {
        return cabIdToCabEvents.get(cabId);
    }

}
