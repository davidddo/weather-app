package de.hdm.weatherapp.interfaces.forecast.week;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import de.hdm.weatherapp.interfaces.common.WeatherItem;

public class DailyWeather {

    @SerializedName("dt")
    public int dateTime;

    @SerializedName("sunrise")
    public int sunrise;

    @SerializedName("sunset")
    public int sunset;

    @SerializedName("moonrise")
    public int moonrise;

    @SerializedName("moonset")
    public int moonset;

    @SerializedName("moon_phase")
    public double moonPhase;

    @SerializedName("temp")
    public Temp temp;

    @SerializedName("feels_like")
    public FeelsLike feelsLike;

    @SerializedName("pressure")
    public double pressure;

    @SerializedName("humidity")
    public int humidity;

    @SerializedName("dew_point")
    public double dewPoint;

    @SerializedName("wind_speed")
    public double windSpeed;

    @SerializedName("wind_deg")
    public int windDeg;

    @SerializedName("wind_gust")
    public double windGust;

    @SerializedName("weather")
    public List<WeatherItem> weather;

    @SerializedName("clouds")
    public int clouds;

    @SerializedName("pop")
    public double pop;

    @SerializedName("rain")
    public double rain;

    @SerializedName("uvi")
    public double uvi;
}

