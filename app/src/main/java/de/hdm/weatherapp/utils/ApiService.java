package de.hdm.weatherapp.utils;

import de.hdm.weatherapp.models.current.CurrentWeatherResponse;
import de.hdm.weatherapp.models.forecast.week.WeekForecastResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    /**
     * Get the current weather of the location
     *
     * @param latitude  Geographical coordinates (latitude)
     * @param longitude Geographical coordinates (latitude)
     * @param units     String units of response
     * @param lang      String language of response
     * @param appId     String api key
     *
     * @return instance of {@link CurrentWeatherResponse}
     */
    @GET("weather")
    Call<CurrentWeatherResponse> getCurrentWeather(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("units") String units,
            @Query("lang") String lang,
            @Query("appid") String appId
    );

    /**
     * Get the weather forecast for the next 7 days
     *
     * @param latitude  Geographical coordinates (latitude)
     * @param longitude Geographical coordinates (latitude)
     * @param units     String units of response
     * @param lang      String language of response
     * @param appId     String api key
     *
     * @return instance of {@link WeekForecastResponse}
     */
    @GET("onecall")
    Call<WeekForecastResponse> getWeekForecast(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("units") String units,
            @Query("lang") String lang,
            @Query("exclude") String exclude,
            @Query("appid") String appId
    );
}
