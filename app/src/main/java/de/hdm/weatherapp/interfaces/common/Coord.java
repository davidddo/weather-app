package de.hdm.weatherapp.interfaces.common;

import com.google.gson.annotations.SerializedName;

public class Coord {
    @SerializedName("lon")
    public double lon;

    @SerializedName("lat")
    public double lat;

    public Coord(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }
}