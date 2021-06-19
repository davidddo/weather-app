package de.hdm.weatherapp.database.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import de.hdm.weatherapp.interfaces.forecast.day.DayForecastResponse;



import androidx.room.TypeConverter;

        import com.google.gson.Gson;
        import com.google.gson.reflect.TypeToken;

        import java.lang.reflect.Type;

        import de.hdm.weatherapp.interfaces.forecast.day.DayForecastResponse;
import de.hdm.weatherapp.interfaces.forecast.week.WeekForecastResponse;

public class WeekForecastConverter {
    @TypeConverter
    public static WeekForecastResponse JsonToWeekForecast(String json) {
        Type type = new TypeToken<WeekForecastResponse>() {
        }.getType();
        return new Gson().fromJson(json, type);
    }

    @TypeConverter
    public static String WeekForecastToJson(WeekForecastResponse response) {
        return new Gson().toJson(response);
    }
}