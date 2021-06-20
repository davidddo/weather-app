package de.hdm.weatherapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.hdm.weatherapp.database.converter.CurrentWeatherConverter;
import de.hdm.weatherapp.database.converter.DateConverter;
import de.hdm.weatherapp.database.converter.DayForecastConverter;
import de.hdm.weatherapp.database.converter.WeekForecastConverter;
import de.hdm.weatherapp.database.dao.CacheDao;
import de.hdm.weatherapp.database.dao.CityDao;
import de.hdm.weatherapp.database.entity.CacheEntity;
import de.hdm.weatherapp.database.entity.CityEntity;
import de.hdm.weatherapp.utils.AppExecutors;
import de.hdm.weatherapp.utils.Utils;

@Database(entities = {CityEntity.class, CacheEntity.class}, exportSchema = false, version = 13)
@TypeConverters({CurrentWeatherConverter.class, DayForecastConverter.class, WeekForecastConverter.class, DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "weather_app";
    private static AppDatabase database;

    public static synchronized AppDatabase getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return database;
    }

    public void initCityDatabase(Context context) {
        if (database.cityDao().getAny() != null) {
            return;
        }

        AppExecutors.getInstance().diskIO().execute(() -> {
            String json = Utils.getJsonFromAssets(context, "cities.list.json");
            Type type = new TypeToken<ArrayList<CityEntity>>() {}.getType();

            List<CityEntity> cities = new Gson().fromJson(json, type);
            database.cityDao().insertMany(cities);
        });
    }

    public abstract CityDao cityDao();
    public abstract CacheDao cacheDao();
}
