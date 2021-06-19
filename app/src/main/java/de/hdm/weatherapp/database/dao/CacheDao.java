package de.hdm.weatherapp.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import de.hdm.weatherapp.database.entity.CacheEntity;
import de.hdm.weatherapp.interfaces.current.CurrentWeatherResponse;
import de.hdm.weatherapp.interfaces.forecast.day.DayForecastResponse;
import de.hdm.weatherapp.interfaces.forecast.week.WeekForecastResponse;

@Dao
public interface CacheDao {
    @Query("SELECT * FROM cache WHERE id = :id")
    CacheEntity getById(int id);

    @Query("SELECT * FROM cache WHERE latitude = :latitude AND longitude = :longitude LIMIT 1")
    CacheEntity getAnyByLatLong(double latitude, double longitude);

    @Query("SELECT * FROM cache WHERE latitude = :latitude AND longitude = :longitude")
    CacheEntity getByLatLong(double latitude, double longitude);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CacheEntity cache);

    @Query("UPDATE cache SET current_weather = :response WHERE latitude = :latitude AND longitude = :longitude")
    void update(double latitude, double longitude, CurrentWeatherResponse response);

    @Query("UPDATE cache SET day_forecast = :response WHERE latitude = :latitude AND longitude = :longitude")
    void update(double latitude, double longitude, DayForecastResponse response);

    @Query("UPDATE cache SET week_forecast = :response WHERE latitude = :latitude AND longitude = :longitude")
    void update(double latitude, double longitude, WeekForecastResponse response);

    @Delete
    void delete(CacheEntity cache);
}
