package de.hdm.weatherapp.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import de.hdm.weatherapp.models.common.Coord;

@Entity
public class CityData {

    @PrimaryKey
    public int uid;

    public int id;

    public String name;

    public String country;

    public Coord coord;

    public CityData(int id, String name, String country, Coord coord){
        this.id = id;
        this.name = name;
        this.country = country;
        this.coord = coord;
    }


}
