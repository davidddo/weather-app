package de.hdm.weatherapp.models.current;

import com.google.gson.annotations.SerializedName;

public class Sys {
    @SerializedName("country")
    public String country;

    @SerializedName("sunrise")
    public int sunrise;

    @SerializedName("sunset")
    public int sunset;

    @SerializedName("message")
    public double message;
}
