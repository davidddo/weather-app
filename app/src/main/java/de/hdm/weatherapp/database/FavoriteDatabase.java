package de.hdm.weatherapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import de.hdm.weatherapp.database.dao.FavoriteDao;
import de.hdm.weatherapp.database.entity.FavoriteEntity;

@Database(entities = {FavoriteEntity.class}, version = 5)
public abstract class FavoriteDatabase extends RoomDatabase {
    public abstract FavoriteDao favoriteDao();
}