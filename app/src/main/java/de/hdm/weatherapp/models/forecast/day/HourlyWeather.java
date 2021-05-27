package de.hdm.weatherapp.models.forecast.day;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import de.hdm.weatherapp.models.common.WeatherItem;
import de.hdm.weatherapp.models.forecast.week.FeelsLike;
import de.hdm.weatherapp.models.forecast.week.Temp;

public class HourlyWeather {
    @SerializedName("dt")
    public int dateTime;

    @SerializedName("temp")
    public double temp;

    @SerializedName("feels_like")
    public double feelsLike;

    @SerializedName("pressure")
    public double pressure;

    @SerializedName("humidity")
    public int humidity;

    @SerializedName("dew_point")
    public double dewPoint;

    @SerializedName("uvi")
    public double uvi;

    @SerializedName("clouds")
    public int clouds;

    @SerializedName("visibility")
    public int visibility;

    @SerializedName("wind_speed")
    public double windSpeed;

    @SerializedName("wind_deg")
    public int windDeg;

    @SerializedName("wind_gust")
    public double windGust;

    @SerializedName("weather")
    public List<WeatherItem> weather;

    @SerializedName("pop")
    public double pop;

}
