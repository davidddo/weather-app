package de.hdm.weatherapp.interfaces.forecast.day;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import de.hdm.weatherapp.interfaces.forecast.ForecastResponse;

public class DayForecastResponse extends ForecastResponse {
    @SerializedName("hourly")
    public List<HourlyWeather> hourly;

}
