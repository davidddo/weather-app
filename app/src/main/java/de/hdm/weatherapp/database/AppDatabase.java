package de.hdm.weatherapp.database;

import android.content.Context;

import androidx.room.Room;

import de.hdm.weatherapp.database.entity.FavoriteEntity;

public class AppDatabase {
    private static AppDatabase instance = null;
    private final CityDatabase cityDatabase;
    private final FavoriteDatabase favoriteDatabase;

    private AppDatabase(Context context) {
        cityDatabase = Room.databaseBuilder(context, CityDatabase.class, "weather_app")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        favoriteDatabase = Room.databaseBuilder(context, FavoriteDatabase.class, "weather_app")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    public static synchronized AppDatabase instance(Context context) {
        if (instance == null) {
            instance = new AppDatabase(context);
        }
        return instance;
    }

    public CityDatabase getCityDatabase() {
        return cityDatabase;
    }

    public FavoriteDatabase getFavoriteDatabase() {
        return favoriteDatabase;
    }
}