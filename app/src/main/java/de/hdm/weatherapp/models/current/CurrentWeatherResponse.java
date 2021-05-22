package de.hdm.weatherapp.models.current;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import de.hdm.weatherapp.models.common.Clouds;
import de.hdm.weatherapp.models.common.Coord;
import de.hdm.weatherapp.models.common.WeatherItem;
import de.hdm.weatherapp.models.common.Wind;

public class CurrentWeatherResponse {

    @SerializedName("dt")
    public int dt;

    @SerializedName("coord")
    public Coord coord;

    @SerializedName("weather")
    public List<WeatherItem> weather;

    @SerializedName("name")
    public String name;

    @SerializedName("cod")
    public int cod;

    @SerializedName("main")
    public Main main;

    @SerializedName("clouds")
    public Clouds clouds;

    @SerializedName("id")
    public int id;

    @SerializedName("sys")
    public Sys sys;

    @SerializedName("base")
    public String base;

    @SerializedName("wind")
    public Wind wind;
}