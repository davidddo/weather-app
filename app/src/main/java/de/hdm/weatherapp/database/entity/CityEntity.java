package de.hdm.weatherapp.database.entity;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import de.hdm.weatherapp.models.common.Coord;

@Entity(tableName = "cities")
public class CityEntity {

    @PrimaryKey
    public int id;
    public String name;
    public String country;

    @Embedded(prefix = "coord_")
    public Coord coord;

    public CityEntity(int id, String name, String country, Coord coord){
        this.id = id;
        this.name = name;
        this.country = country;
        this.coord = coord;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        return false;
    }
}
