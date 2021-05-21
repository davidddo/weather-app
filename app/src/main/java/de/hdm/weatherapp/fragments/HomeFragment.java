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

import de.hdm.weatherapp.R;
import de.hdm.weatherapp.models.currentweather.CurrentWeatherResponse;
import de.hdm.weatherapp.utils.ApiClient;
import de.hdm.weatherapp.utils.ApiService;
import de.hdm.weatherapp.views.CurrentWeatherView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements LocationListener {
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private final String[] requiredPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    private Context context;
    private CurrentWeatherView currentWeatherView;

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

        loadCurrentWeather(latitude, longitude);
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
        locationManager.requestLocationUpdates(provider, 1000, 100F, this);
    }

    private void loadCurrentWeather(double latitude, double longitude) {
        Call<CurrentWeatherResponse> call = apiService.getCurrentWeather(latitude, longitude, "metric", "de", ApiClient.API_KEY);
        call.enqueue(new Callback<CurrentWeatherResponse>() {
            @Override
            public void onResponse(Call<CurrentWeatherResponse> call, Response<CurrentWeatherResponse> response) {
                currentWeatherView.setWeather(response.body());
            }

            @Override
            public void onFailure(Call<CurrentWeatherResponse> call, Throwable t) {
                Log.e("Oh noo.", t.getMessage());
            }
        });
    }
}