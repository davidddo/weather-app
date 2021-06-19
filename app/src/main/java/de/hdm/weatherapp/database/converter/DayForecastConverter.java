package de.hdm.weatherapp.database.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import de.hdm.weatherapp.interfaces.forecast.day.DayForecastResponse;

public class DayForecastConverter {
    @TypeConverter
    public static DayForecastResponse JsonToDayForecast(String json) {
        Type type = new TypeToken<DayForecastResponse>() {
        }.getType();
        return new Gson().fromJson(json, type);
    }

    @TypeConverter
    public static String DayForecastToJson(DayForecastResponse response) {
        return new Gson().toJson(response);
    }
}