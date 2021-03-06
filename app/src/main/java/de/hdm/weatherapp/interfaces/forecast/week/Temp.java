package de.hdm.weatherapp.interfaces.forecast.week;

import com.google.gson.annotations.SerializedName;

public class Temp {
    @SerializedName("day")
    public double day;

    @SerializedName("min")
    public double min;

    @SerializedName("max")
    public double max;

    @SerializedName("night")
    public double night;

    @SerializedName("eve")
    public double eve;

    @SerializedName("morn")
    public double morn;
}
