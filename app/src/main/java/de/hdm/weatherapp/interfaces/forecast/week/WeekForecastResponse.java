package de.hdm.weatherapp.interfaces.forecast.week;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import de.hdm.weatherapp.interfaces.forecast.ForecastResponse;

public class WeekForecastResponse extends ForecastResponse {
    @SerializedName("daily")
    public List<DailyWeather> daily;
}
