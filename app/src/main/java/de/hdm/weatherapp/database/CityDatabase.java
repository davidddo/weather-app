package de.hdm.weatherapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import de.hdm.weatherapp.database.dao.CityDao;
import de.hdm.weatherapp.database.entity.CityEntity;

@Database(entities = {CityEntity.class}, version = 4)
public abstract class CityDatabase extends RoomDatabase {
    public abstract CityDao cityDao();
}