package de.hdm.weatherapp.database;

import android.content.Context;

import androidx.room.Room;

public class AppDatabase {
    private static AppDatabase instance = null;
    private final CityDatabase cityDatabase;

    private AppDatabase(Context context) {
        cityDatabase = Room.databaseBuilder(context, CityDatabase.class, "weather_app")
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
}