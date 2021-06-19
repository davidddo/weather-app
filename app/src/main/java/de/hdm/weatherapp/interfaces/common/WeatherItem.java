package de.hdm.weatherapp.interfaces.common;

import com.google.gson.annotations.SerializedName;

public class WeatherItem {

    @SerializedName("id")
    public int id;

    @SerializedName("main")
    public String main;

    @SerializedName("description")
    public String description;

    @SerializedName("icon")
    public String icon;
}