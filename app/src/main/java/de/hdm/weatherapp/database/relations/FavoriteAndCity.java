package de.hdm.weatherapp.database.relations;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Relation;

import java.util.List;

import de.hdm.weatherapp.database.entity.CityEntity;
import de.hdm.weatherapp.database.entity.FavoriteEntity;

class FavoriteAndCity {
    @Embedded
    CityEntity cityEntity;
    @Relation(
            parentColumn = "id",
            entityColumn = "cityId"
    )
    List<FavoriteEntity> favoriteEntities;
}