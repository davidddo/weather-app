package de.hdm.weatherapp.utils;

import de.hdm.weatherapp.interfaces.current.CurrentWeatherResponse;
import de.hdm.weatherapp.interfaces.forecast.day.DayForecastResponse;
import de.hdm.weatherapp.interfaces.forecast.week.WeekForecastResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    /**
     * Get the current weather of the location
     *
     * @param latitude  Geographical coordinates (latitude)
     * @param longitude Geographical coordinates (latitude)
     * @param appId     String api key
     * @return instance of {@link CurrentWeatherResponse}
     */
    @GET("weather?units=metric&lang=de")
    Call<CurrentWeatherResponse> getCurrentWeather(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("appid") String appId
    );

    /**
     * Get the weather forecast for the next 7 days
     *
     * @param latitude  Geographical coordinates (latitude)
     * @param longitude Geographical coordinates (latitude)
     * @param appId     String api key
     * @return instance of {@link WeekForecastResponse}
     */
    @GET("onecall?exlude=current,minutely,hourly,alerts&units=metric&lang=de")
    Call<WeekForecastResponse> getWeekForecast(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("appid") String appId
    );

    @GET("onecall?exlude=current,daily,minutely,alerts&units=metric&lang=de")
    Call<DayForecastResponse> getDayForecast(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("appid") String appId
    );
}
