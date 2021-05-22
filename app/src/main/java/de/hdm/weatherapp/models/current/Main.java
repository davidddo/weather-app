package de.hdm.weatherapp.models.current;

import com.google.gson.annotations.SerializedName;

public class Main {

    @SerializedName("temp")
    public double temp;

    @SerializedName("feels_like")
    public double feelsLike;

    @SerializedName("temp_min")
    public double tempMin;

    @SerializedName("grnd_level")
    public double grndLevel;

    @SerializedName("humidity")
    public int humidity;

    @SerializedName("pressure")
    public double pressure;

    @SerializedName("sea_level")
    public double seaLevel;

    @SerializedName("temp_max")
    public double tempMax;
}