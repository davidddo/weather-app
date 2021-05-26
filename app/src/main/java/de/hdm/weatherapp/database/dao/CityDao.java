package de.hdm.weatherapp.database.dao;

import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import de.hdm.weatherapp.database.entity.CityEntity;

@Dao
public interface CityDao {

    @Query("SELECT * FROM cities")
    List<CityEntity> getAll();

    @Query("SELECT * FROM cities WHERE id = :id")
    CityEntity getById(int id);

    @Query("SELECT * FROM cities WHERE name LIKE '%' || :query || '%'")
    PagingSource<Integer, CityEntity> pagingSource(String query);

    @Insert
    void insert(CityEntity city);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMany(List<CityEntity> cities);

    @Delete
    void delete(CityEntity cityDetail);
}
