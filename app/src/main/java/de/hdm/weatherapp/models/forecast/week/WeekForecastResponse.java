package de.hdm.weatherapp.models.forecast.week;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import de.hdm.weatherapp.models.forecast.ForecastResponse;

public class WeekForecastResponse extends ForecastResponse {
    @SerializedName("daily")
    public List<DailyWeather> daily;
}
