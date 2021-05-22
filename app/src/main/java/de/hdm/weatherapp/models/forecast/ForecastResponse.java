package de.hdm.weatherapp.models.forecast;

import com.google.gson.annotations.SerializedName;

public class ForecastResponse {
    @SerializedName("lat")
    public double lat;

    @SerializedName("lon")
    public double lon;

    @SerializedName("timezone")
    public String timezone;

    @SerializedName("timezone_offset")
    public int timezoneOffset;
}
