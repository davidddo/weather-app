package de.hdm.weatherapp.models;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.annotation.NonNull;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import de.hdm.weatherapp.interfaces.Location;

public class HomeViewModel extends ViewModel {
    private MediatorLiveData<Location> liveData;
    private LocationLiveData locationLiveData;

    public HomeViewModel() {
        System.out.println("wdadawkdawnofwaiofaf");
    }

    public LiveData<Location> getLocation() {
        if (liveData == null) {
            liveData = new MediatorLiveData<>();
            locationLiveData = new LocationLiveData();

            liveData.addSource(locationLiveData, location -> {
                liveData.setValue(location);
            });
        }

        return liveData;
    }

    public void requestLocationUpdates(Context context) {
        locationLiveData.requestLocationUpdates(context);
    }

    private static class LocationLiveData extends LiveData<Location> implements LocationListener {
        private LocationManager locationManager;

        @Override
        protected void onInactive() {
            super.onInactive();
            if (locationManager != null) {
                this.locationManager.removeUpdates(this);
            }
        }

        @Override
        public void onLocationChanged(@NonNull android.location.Location location) {
            setValue(new Location(location.getLatitude(), location.getLongitude()));
        }

        @SuppressLint("MissingPermission")
        public void requestLocationUpdates(Context context) {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            String provider = locationManager.getBestProvider(new Criteria(), false);
            locationManager.requestLocationUpdates(provider, 500, 100F, this);
        }
    }
}
