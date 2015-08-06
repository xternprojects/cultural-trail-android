package com.xtern.cultural_trail.models;

import java.io.Serializable;

/**
 * Created by kyle on 6/29/15.
 */
public class Location implements Serializable {
    public final double lat;
    public final double lng;

    public Location(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }
}
