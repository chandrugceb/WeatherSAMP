package com.example.chand.weathersamp.utils;

/**
 * Created by chand on 08-10-2017.
 */

public class Location {
    private Double Latitude;
    private String Location_Name;
    private Double Longitude;

    public Location() {
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public String getLocation_Name() {
        return Location_Name;
    }

    public void setLocation_Name(String location_Name) {
        Location_Name = location_Name;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public Location(Double latitude, String location_Name, Double longitude) {
        Latitude = latitude;
        Location_Name = location_Name;
        Longitude = longitude;
    }
}
