package de.hdm.weatherapp.database.repository;

import android.content.Context;

import de.hdm.weatherapp.database.AppDatabase;
import de.hdm.weatherapp.database.dao.CacheDao;
import de.hdm.weatherapp.database.entity.CacheEntity;
import de.hdm.weatherapp.interfaces.Location;
import de.hdm.weatherapp.interfaces.current.CurrentWeatherResponse;
import de.hdm.weatherapp.interfaces.forecast.day.DayForecastResponse;
import de.hdm.weatherapp.interfaces.forecast.week.WeekForecastResponse;
import de.hdm.weatherapp.utils.AppExecutors;

public class CacheRepository {
    private final AppDatabase database;
    private final CacheDao cacheDao;

    public CacheRepository(Context context) {
        database = AppDatabase.getInstance(context);
        cacheDao = database.cacheDao();
    }

    public CacheEntity getByLocation(Location location) {
        return this.cacheDao.getByLatLong(location.latitude, location.longitude);
    }

    public void insertOrUpdate(double latitude, double longitude, CurrentWeatherResponse response) {
        AppExecutors.getInstance().diskIO().execute(() -> database.runInTransaction(() -> {
            CacheEntity any = cacheDao.getAnyByLatLong(latitude, longitude);
            if (any == null) {
                cacheDao.insert(new CacheEntity(latitude, longitude, response));
            } else {
                cacheDao.update(latitude, longitude, response);
            }
        }));
    }

    public void insertOrUpdate(double latitude, double longitude, DayForecastResponse response) {
        AppExecutors.getInstance().diskIO().execute(() -> database.runInTransaction(() -> {
            CacheEntity any = cacheDao.getAnyByLatLong(latitude, longitude);
            if (any == null) {
                cacheDao.insert(new CacheEntity(latitude, longitude, response));
            } else {
                cacheDao.update(latitude, longitude, response);
            }
        }));
    }

    public void insertOrUpdate(double latitude, double longitude, WeekForecastResponse response) {
        AppExecutors.getInstance().diskIO().execute(() -> database.runInTransaction(() -> {
            CacheEntity any = cacheDao.getAnyByLatLong(latitude, longitude);
            if (any == null) {
                cacheDao.insert(new CacheEntity(latitude, longitude, response));
            } else {
                cacheDao.update(latitude, longitude, response);
            }
        }));
    }

    public boolean checkIfCacheIsValid(CacheEntity cache) {
        if (cache.currentWeather == null || cache.dayForecast == null || cache.weekForecast == null) {
            this.cacheDao.delete(cache);
            return false;
        }

        return true;
    }
}
