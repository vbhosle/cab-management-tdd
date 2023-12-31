package com.bookmycab.events;

import com.bookmycab.models.Booking;
import com.bookmycab.models.CabSnapshot;

import java.util.Objects;
import java.util.UUID;

public class BookingEvent {
    private final UUID id = UUID.randomUUID();
    private final CabSnapshot cabSnapshot;
    private final long bookedAt;

    public BookingEvent(CabSnapshot cabSnapshot, long bookedAt) {
        this.cabSnapshot = cabSnapshot;
        this.bookedAt = bookedAt;
    }

    public static BookingEvent from(Booking booking) {
        return new BookingEvent(booking.getCabSnapshot(), booking.getBookedAt());
    }

    public CabSnapshot getCabSnapshot() {
        return this.cabSnapshot;
    }

    public long getBookedAt() {
        return this.bookedAt;
    }

    public UUID getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingEvent that = (BookingEvent) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
