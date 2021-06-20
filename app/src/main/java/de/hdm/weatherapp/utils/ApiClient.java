package de.hdm.weatherapp.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import de.hdm.weatherapp.interfaces.common.Coord;
import de.hdm.weatherapp.interfaces.current.CurrentWeatherResponse;
import de.hdm.weatherapp.interfaces.forecast.day.DayForecastResponse;
import de.hdm.weatherapp.interfaces.forecast.week.WeekForecastResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    public static final String API_KEY = "b084815f23310d91515df76000554abe";

    private static ApiClient instance;
    private static Retrofit retrofit;
    private static ApiService apiService;

    public static ApiClient getClient() {
        if (instance == null) {
            instance = new ApiClient();
        }

        if (retrofit == null) {
            Gson gson = new GsonBuilder().setLenient().create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        if (apiService == null) {
            apiService = retrofit.create(ApiService.class);
        }

        return instance;
    }

    public Call<CurrentWeatherResponse> loadCurrentWeather(double latitude, double longitude, ResponseListener<CurrentWeatherResponse> listener) {
        Call<CurrentWeatherResponse> call = apiService.getCurrentWeather(latitude, longitude, ApiClient.API_KEY);
        return enqueueApiCall(call, listener);
    }

    public Call<WeekForecastResponse> loadWeekForecast(double latitude, double longitude, ResponseListener<WeekForecastResponse> listener) {
        Call<WeekForecastResponse> call = apiService.getWeekForecast(latitude, longitude, ApiClient.API_KEY);
        return enqueueApiCall(call, listener);
    }

    public Call<DayForecastResponse> loadDayForecast(double latitude, double longitude, ResponseListener<DayForecastResponse> listener) {
        Call<DayForecastResponse> call = apiService.getDayForecast(latitude, longitude, ApiClient.API_KEY);
        return enqueueApiCall(call, listener);
    }

    private <T> Call<T> enqueueApiCall(Call<T> call, ResponseListener<T> listener) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(@NotNull Call<T> call, @NotNull Response<T> response) {
                if (response.body() != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<T> call, @NotNull Throwable throwable) {
                Log.e("WeatherAPI", throwable.getMessage());
            }
        });

        return call;
    }

    public interface ResponseListener<T> {
        void onResponse(T response);
    }
}