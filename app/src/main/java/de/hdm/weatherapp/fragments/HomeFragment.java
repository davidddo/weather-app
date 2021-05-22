package de.hdm.weatherapp.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import de.hdm.weatherapp.R;
import de.hdm.weatherapp.models.current.CurrentWeatherResponse;
import de.hdm.weatherapp.models.forecast.ForecastResponse;
import de.hdm.weatherapp.models.forecast.week.WeekForecastResponse;
import de.hdm.weatherapp.utils.ApiClient;
import de.hdm.weatherapp.utils.ApiService;
import de.hdm.weatherapp.views.CurrentWeatherView;

import de.hdm.weatherapp.views.WeatherDetailsView;
import de.hdm.weatherapp.views.WeekForecastView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements LocationListener {
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private final String[] requiredPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    private final double[][] fallbackCities = {{48.783333, 9.183333}, {40.730610, -73.935242}, {51.509865, -0.118092}, {38.736946, -9.142685}};

    private Context context;
    private CurrentWeatherView currentWeatherView;
    private WeatherDetailsView weatherDetailsView;
    private WeekForecastView weekForecastView;


    private LocationManager locationManager;
    private ApiService apiService;

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        apiService = ApiClient.getClient().create(ApiService.class);

        boolean hasPermission = checkLocationPermissions();
        if (hasPermission) {
            requestLocationUpdates();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        currentWeatherView = view.findViewById(R.id.current_weather_view);
        weatherDetailsView = view.findViewById(R.id.weather_details);
        weekForecastView = view.findViewById(R.id.week_forecast);

        return view;
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        final double latitude = location.getLatitude();
        final double longitude = location.getLongitude();

        loadWeatherData(latitude, longitude);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String permissions[], @NotNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        requestLocationUpdates();
                    }
                } else {
                    // Todo: Show Toast or snack bar.

                    double[] location = getRandomCityLocation();
                    loadWeatherData(location[0], location[1]);
                }
                return;
            }

        }
    }

    private boolean checkLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(requiredPermissions, REQUEST_LOCATION_PERMISSION);
            return false;
        }

        return true;
    }

    @RequiresPermission(anyOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    private void requestLocationUpdates() {
        final String provider = locationManager.getBestProvider(new Criteria(), false);
        locationManager.requestLocationUpdates(provider, 500, 100F, this);
    }

    private void loadWeatherData(double latitude, double longitude) {
        loadCurrentWeather(latitude, longitude);
        loadWeekForecast(latitude, longitude);
    }

    private void loadCurrentWeather(double latitude, double longitude) {
        Call<CurrentWeatherResponse> call = apiService.getCurrentWeather(latitude, longitude, "metric", "de", ApiClient.API_KEY);
        call.enqueue(new Callback<CurrentWeatherResponse>() {
            @Override
            public void onResponse(Call<CurrentWeatherResponse> call, Response<CurrentWeatherResponse> response) {
                CurrentWeatherResponse weatherResponse = response.body();
                if (weatherResponse != null) {
                    currentWeatherView.setWeather(weatherResponse);
                    weatherDetailsView.setWeather(weatherResponse);
                }
            }

            @Override
            public void onFailure(Call<CurrentWeatherResponse> call, Throwable t) {
                Log.e("Oh noo.", t.getMessage());
            }
        });
    }

    private void loadWeekForecast(double latitude, double longitude) {
        Call<WeekForecastResponse> call = apiService.getWeekForecast(latitude, longitude, "metric", "de", "current,minutely,hourly,alerts", ApiClient.API_KEY);
        call.enqueue(new Callback<WeekForecastResponse>() {
            @Override
            public void onResponse(Call<WeekForecastResponse> call, Response<WeekForecastResponse> response) {
                WeekForecastResponse forecastResponse = response.body();
                if (forecastResponse != null) {
                    weekForecastView.setWeekForecast(forecastResponse);
                }
            }

            @Override
            public void onFailure(Call<WeekForecastResponse> call, Throwable t) {
                Log.e("Oh noo.", t.getMessage());
            }
        });
    }

    private double[] getRandomCityLocation() {
        return fallbackCities[new Random().nextInt(fallbackCities.length)];
    }
}