package de.hdm.weatherapp.models.cities;

import com.google.gson.annotations.SerializedName;

import de.hdm.weatherapp.models.common.Coord;

public class City {

    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("country")
    public String country;

    @SerializedName("coord")
    public Coord coord;


    @Override
    public String toString() {
        return "CityData{" +
                "id=" + id +
                ", \n name='" + name + '\'' +
                ", \n country='" + country + '\'' +
                ", \n coord=" + coord +
                '}';
    }

}
