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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import de.hdm.weatherapp.R;

import de.hdm.weatherapp.views.WeatherView;

public class HomeFragment extends Fragment implements LocationListener {
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private final String[] requiredPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    private final double[][] fallbackCities = {{48.783333, 9.183333}, {40.730610, -73.935242}, {51.509865, -0.118092}, {38.736946, -9.142685}};

    private Context context;
    private WeatherView weatherView;

    private LocationManager locationManager;

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        boolean hasPermission = checkLocationPermissions();
        if (hasPermission) {
            requestLocationUpdates();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        weatherView = view.findViewById(R.id.weather_view);

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

        weatherView.bindLocation(latitude, longitude);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    requestLocationUpdates();
                }
            } else {
                // Todo: Show Toast or snack bar.

                double[] location = getRandomCityLocation();
                weatherView.bindLocation(location[0], location[1]);
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

    private double[] getRandomCityLocation() {
        return fallbackCities[new Random().nextInt(fallbackCities.length)];
    }
}