package com.plotprojects.coupons;

import android.location.Location;

public class Coupon {

    private Location location;
    private String message;
    private String category;

    public Coupon(Location location, String message, String category) {
        this.location = location;
        this.message = message;
        this.category = category;
    }

    public Location getLocation() {
        return location;
    }

    public String getMessage() {
        return message;
    }

    public String getCategory() {
        return category;
    }
}
