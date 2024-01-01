package com.bookmycab.events;

import com.bookmycab.models.Booking;

public class BookingEvent {

    private final Booking booking;

    public BookingEvent(Booking booking) {
        this.booking = booking;
    }

    public Booking getBooking() {
        return this.booking;
    }

    public long getBookedAt() {
        return this.booking.getBookedAt();
    }
}
