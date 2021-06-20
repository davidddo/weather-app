package de.hdm.weatherapp.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import de.hdm.weatherapp.database.converter.CurrentWeatherConverter;
import de.hdm.weatherapp.database.converter.DayForecastConverter;
import de.hdm.weatherapp.database.converter.WeekForecastConverter;
import de.hdm.weatherapp.interfaces.current.CurrentWeatherResponse;
import de.hdm.weatherapp.interfaces.forecast.day.DayForecastResponse;
import de.hdm.weatherapp.interfaces.forecast.week.WeekForecastResponse;

@Entity(tableName = "cache")
public class CacheEntity {

    @PrimaryKey(autoGenerate = true)
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

    public Date timestamp;

    public CacheEntity(int id, double latitude, double longitude, CurrentWeatherResponse currentWeather, DayForecastResponse dayForecast, WeekForecastResponse weekForecast, Date timestamp) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.currentWeather = currentWeather;
        this.dayForecast = dayForecast;
        this.weekForecast = weekForecast;
        this.timestamp = timestamp;
    }

    @Ignore()
    public CacheEntity(double latitude, double longitude, CurrentWeatherResponse currentWeather, Date timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.currentWeather = currentWeather;
        this.timestamp = timestamp;
    }

    @Ignore()
    public CacheEntity(double latitude, double longitude, DayForecastResponse dayForecast, Date timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.dayForecast = dayForecast;
        this.timestamp = timestamp;
    }

    @Ignore()
    public CacheEntity(double latitude, double longitude, WeekForecastResponse weekForecast, Date timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.weekForecast = weekForecast;
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        return false;
    }
}
