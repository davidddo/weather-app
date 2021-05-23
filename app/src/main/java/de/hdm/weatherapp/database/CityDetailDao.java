package de.hdm.weatherapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import de.hdm.weatherapp.models.cities.City;

@Dao
public interface CityDetailDao {

    @Query("SELECT * FROM citydata")
    List<City> getAll();

    @Query("SELECT * FROM citydata WHERE id = :id")
    City[] find(int id);

    @Insert
    void insert(City cityDetail);

    @Delete
    void delete(City cityDetail);

}
