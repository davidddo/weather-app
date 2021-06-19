package de.hdm.weatherapp.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import de.hdm.weatherapp.database.converter.CurrentWeatherConverter;
import de.hdm.weatherapp.database.converter.DayForecastConverter;
import de.hdm.weatherapp.database.converter.WeekForecastConverter;
import de.hdm.weatherapp.interfaces.current.CurrentWeatherResponse;
import de.hdm.weatherapp.interfaces.forecast.day.DayForecastResponse;
import de.hdm.weatherapp.interfaces.forecast.week.WeekForecastResponse;

@Entity(tableName = "cache", primaryKeys = {"id", "latitude", "longitude"})
public class CacheEntity {
    public int id;
    public double latitude;
    public double longitude;

    @ColumnInfo(name = "current_weather")
    @TypeConverters(CurrentWeatherConverter.class)
    public CurrentWeatherResponse currentWeather;

    @ColumnInfo(name = "day_forecast")
    @TypeConverters(DayForecastConverter.class)
    public DayForecastResponse dayForecast;

    @ColumnInfo(name = "week_forecast")
    @TypeConverters(WeekForecastConverter.class)
    public WeekForecastResponse weekForecast;

    public CacheEntity(int id, double latitude, double longitude, CurrentWeatherResponse currentWeather, DayForecastResponse dayForecast, WeekForecastResponse weekForecast) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.currentWeather = currentWeather;
        this.dayForecast = dayForecast;
        this.weekForecast = weekForecast;
    }

    @Ignore()
    public CacheEntity(double latitude, double longitude, CurrentWeatherResponse currentWeather) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.currentWeather = currentWeather;
    }

    @Ignore()
    public CacheEntity(double latitude, double longitude, DayForecastResponse dayForecast) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.dayForecast = dayForecast;
    }

    @Ignore()
    public CacheEntity(double latitude, double longitude, WeekForecastResponse weekForecast) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.weekForecast = weekForecast;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        return false;
    }
}
