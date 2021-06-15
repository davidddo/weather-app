package de.hdm.weatherapp.database.dao;

import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import de.hdm.weatherapp.database.entity.CityEntity;
import de.hdm.weatherapp.database.entity.FavoriteEntity;


@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM favorites")
    List<FavoriteEntity> getAll();

    @Query("SELECT * FROM favorites WHERE cityId = :cityId")
    FavoriteEntity getById(int cityId);

    @Query("SELECT * FROM favorites WHERE cityId LIKE '%' || :query || '%'")
    PagingSource<Integer, FavoriteEntity> pagingSource(int query);

    @Insert()
    void insert(FavoriteEntity favorite);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMany(List<FavoriteEntity> favorites);

    @Delete
    void delete(FavoriteEntity favoriteDetail);



}
