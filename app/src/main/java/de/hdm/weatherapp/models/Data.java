package de.hdm.weatherapp.models;

import java.util.List;

public class Data {
    String cod;
    int message;
    int cnt;
    List<WeatherData> list;
    City city;

    public Data(Data data){
        this.cod = data.cod;
        this.message = data.message;
        this.cnt = data.cnt;
        this.list = data.list;
        this.city = data.city;
    }

}