package de.hdm.weatherapp.database.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import de.hdm.weatherapp.interfaces.current.CurrentWeatherResponse;

public class CurrentWeatherConverter {
    @TypeConverter
    public static CurrentWeatherResponse JsonToCurrentWeather(String json) {
        Type type = new TypeToken<CurrentWeatherResponse>() {
        }.getType();
        return new Gson().fromJson(json, type);
    }

    @TypeConverter
    public static String CurrentWeatherToJson(CurrentWeatherResponse response) {
        return new Gson().toJson(response);
    }
}