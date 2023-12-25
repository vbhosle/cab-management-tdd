package com.bookmycab;

public class Cab {
    private final String id;

    private final CabState state;

    private final String city;

    public Cab(String id, CabState state, String city) {
        this.id = id;
        this.state = state;
        this.city = city;
    }

    public CabState getState() {
        return this.state;
    }

    public String getCity() {
        return this.city;
    }

    public String getId() {
        return this.id;
    }
}
