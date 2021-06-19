package de.hdm.weatherapp.database.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.hdm.weatherapp.database.AppDatabase;
import de.hdm.weatherapp.database.dao.CityDao;
import de.hdm.weatherapp.database.entity.CityEntity;

public class CityRepository {
    private final CityDao cityDao;

    public CityRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        cityDao = database.cityDao();
    }

    public LiveData<List<CityEntity>> getAllFavourites() {
        return this.cityDao.getAllFavourites();
    }

    public LiveData<CityEntity> getById(int id) {
        return this.cityDao.getById(id);
    }

    public void update(int id, boolean saved) {
        this.cityDao.update(id, saved);
    }
}
