package de.hdm.weatherapp.interfaces.current;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import de.hdm.weatherapp.interfaces.Response;
import de.hdm.weatherapp.interfaces.common.Clouds;
import de.hdm.weatherapp.interfaces.common.Coord;
import de.hdm.weatherapp.interfaces.common.WeatherItem;
import de.hdm.weatherapp.interfaces.common.Wind;

public class CurrentWeatherResponse implements Response {

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