package de.hdm.weatherapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import de.hdm.weatherapp.database.entity.CityEntity;

@Dao
public interface CityDao {
    @Query("SELECT * FROM cities WHERE saved = 1")
    LiveData<List<CityEntity>> getAllFavourites();

    @Query("SELECT * FROM cities LIMIT 1")
    CityEntity getAny();

    @Query("SELECT * FROM cities WHERE id = :id")
    LiveData<CityEntity> getById(int id);

    @Query("SELECT * FROM cities WHERE name LIKE '%' || :query || '%'")
    PagingSource<Integer, CityEntity> pagingSource(String query);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMany(List<CityEntity> cities);

    @Query("UPDATE cities SET saved = :saved WHERE id = :id")
    void update(int id, boolean saved);
}
