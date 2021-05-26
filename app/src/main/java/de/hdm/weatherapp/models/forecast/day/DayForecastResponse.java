package de.hdm.weatherapp.models.forecast.day;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import de.hdm.weatherapp.models.forecast.ForecastResponse;
import de.hdm.weatherapp.models.forecast.week.DailyWeather;

public class DayForecastResponse extends ForecastResponse {
    @SerializedName("hourly")
    public List<HourlyWeather> hourly;

}
