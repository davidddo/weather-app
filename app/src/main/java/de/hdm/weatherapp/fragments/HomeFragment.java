package de.hdm.weatherapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.hdm.weatherapp.R;
import de.hdm.weatherapp.models.currentweather.CurrentWeatherResponse;
import de.hdm.weatherapp.utils.ApiClient;
import de.hdm.weatherapp.utils.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private ApiService apiService;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiService = ApiClient.getClient().create(ApiService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void getCurrentWeather(String city) {
        final String apiKey = ApiClient.API_KEY;
        Call<CurrentWeatherResponse> call = apiService.getCurrentWeather(city, "metric", "de", apiKey);
        call.enqueue(new Callback<CurrentWeatherResponse>() {
            @Override
            public void onResponse(Call<CurrentWeatherResponse> call, Response<CurrentWeatherResponse> response) {
                System.out.println(response);
            }

            @Override
            public void onFailure(Call<CurrentWeatherResponse> call, Throwable t) {
                Log.e("Oh noo.", t.getMessage());
            }
        });
    }
}