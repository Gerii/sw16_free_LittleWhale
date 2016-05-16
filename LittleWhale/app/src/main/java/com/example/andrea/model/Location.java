package com.example.andrea.model;

/**
 * Created by clemens on 28.04.16.
 */
public class Location {
    public Long id;
    public String placeName;
    public Double latitude;
    public Double longitude;

    Location (String name, Double lat, Double lon){
        this.id = Long.valueOf(0);
        this.placeName = name;
        this.latitude = lat;
        this.longitude = lon;
    }

    Location (Long id, String name, Double lat, Double lon){
        this.id = id;
        this.placeName = name;
        this.latitude = lat;
        this.longitude = lon;
    }
}
