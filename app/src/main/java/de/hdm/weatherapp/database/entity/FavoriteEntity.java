package de.hdm.weatherapp.database.entity;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import de.hdm.weatherapp.R;
import de.hdm.weatherapp.models.common.Coord;

import static androidx.room.ForeignKey.CASCADE;


@Entity(tableName = "favorites")
public class FavoriteEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int cityId;
    public int minTempView;
    public int maxTempView;


    public FavoriteEntity(int cityId, int minTempView, int maxTempView) {
        this.cityId = cityId;
        this.minTempView = minTempView;
        this.maxTempView = maxTempView;
    }


    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        return false;
    }

    @Override
    public String toString() {
        return "FavoriteEntity{" +
                "id=" + id +
                ", cityId=" + cityId +
                ", minTempView=" + minTempView +
                ", maxTempView=" + maxTempView +
                '}';
    }
}
