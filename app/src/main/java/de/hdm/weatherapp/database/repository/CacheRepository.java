package de.hdm.weatherapp.database.repository;

import android.content.Context;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.hdm.weatherapp.database.AppDatabase;
import de.hdm.weatherapp.database.dao.CacheDao;
import de.hdm.weatherapp.database.entity.CacheEntity;
import de.hdm.weatherapp.interfaces.Location;
import de.hdm.weatherapp.interfaces.current.CurrentWeatherResponse;
import de.hdm.weatherapp.interfaces.forecast.day.DayForecastResponse;
import de.hdm.weatherapp.interfaces.forecast.week.WeekForecastResponse;
import de.hdm.weatherapp.utils.AppExecutors;

public class CacheRepository {
    private static final int TEN_MINUTES = 10 * 60 * 1000;

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
        insertOrUpdate(latitude, longitude, (insert, timestamp) -> {
            if (insert) cacheDao.insert(new CacheEntity(latitude, longitude, response, timestamp));
            else cacheDao.update(latitude, longitude, response);
        });
    }

    public void insertOrUpdate(double latitude, double longitude, DayForecastResponse response) {
        insertOrUpdate(latitude, longitude, (insert, timestamp) -> {
            if (insert) cacheDao.insert(new CacheEntity(latitude, longitude, response, timestamp));
            else cacheDao.update(latitude, longitude, response);
        });
    }

    public void insertOrUpdate(double latitude, double longitude, WeekForecastResponse response) {
        insertOrUpdate(latitude, longitude, (insert, timestamp) -> {
            if (insert) cacheDao.insert(new CacheEntity(latitude, longitude, response, timestamp));
            else cacheDao.update(latitude, longitude, response);
        });
    }

    private void insertOrUpdate(double latitude, double longitude, InsertOrUpdateListener listener) {
        AppExecutors.getInstance().diskIO().execute(() -> database.runInTransaction(() -> {
            CacheEntity any = cacheDao.getAnyByLatLong(latitude, longitude);
            Date timestamp = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5));
            listener.on(any == null, timestamp);
        }));
    }

    public boolean checkIfCacheIsValid(CacheEntity cache) {
        long tenAgo = System.currentTimeMillis() - TEN_MINUTES;
        long timestamp = cache.timestamp.getTime();

        if (cache.currentWeather == null
                || cache.dayForecast == null
                || cache.weekForecast == null
                || timestamp < tenAgo) {
            this.cacheDao.delete(cache);
            System.out.println("Delete");
            return false;
        }

        return true;
    }

    interface InsertOrUpdateListener {
        void on(boolean insert, Date timestamp);
    }
}
