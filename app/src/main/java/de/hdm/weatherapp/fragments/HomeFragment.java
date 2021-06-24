package de.hdm.weatherapp.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;

import de.hdm.weatherapp.R;

import de.hdm.weatherapp.interfaces.Location;
import de.hdm.weatherapp.models.SharedLocationViewModel;
import de.hdm.weatherapp.views.WeatherView;

public class HomeFragment extends Fragment {
    private final String[] requiredPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private final double[] fallbackCity = {48.783333, 9.183333};

    private SharedLocationViewModel model;
    private WeatherView weatherView;

    private final ActivityResultLauncher<String[]> requestPermissions =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
                final Boolean[] results = permissions.values().toArray(new Boolean[0]);
                final boolean allGranted = Arrays.stream(results).allMatch(Boolean::valueOf);

                if (allGranted) {
                    model.requestLocationUpdates(requireContext());
                } else {
                    weatherView.bindLocation(getFallbackCityLocation());
                }
            });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(SharedLocationViewModel.class);
        model.getLocation().observe(this, location -> weatherView.bindLocation(location));

        checkPermissions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        weatherView = view.findViewById(R.id.weather_view);

        return view;
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission_group.LOCATION) == PackageManager.PERMISSION_GRANTED) {
            model.requestLocationUpdates(requireContext());
        } else {
            requestPermissions.launch(requiredPermissions);
        }
    }

    private Location getFallbackCityLocation() {
        return new Location(fallbackCity[0], fallbackCity[1]);
    }
}