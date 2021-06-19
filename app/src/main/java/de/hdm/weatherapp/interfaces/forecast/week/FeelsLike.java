package de.hdm.weatherapp.interfaces.forecast.week;

import com.google.gson.annotations.SerializedName;

public class FeelsLike {
    @SerializedName("day")
    public double day;

    @SerializedName("night")
    public double night;

    @SerializedName("eve")
    public double eve;

    @SerializedName("morn")
    public double morn;
}
