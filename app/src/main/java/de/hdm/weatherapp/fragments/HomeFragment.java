package de.hdm.weatherapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.hdm.weatherapp.R;
import de.hdm.weatherapp.models.Weather;

public class HomeFragment extends Fragment {

    public HomeFragment() { }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeathercardFragment weathercardFragment = new WeathercardFragment();

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.currentWeatherData, weathercardFragment).commit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}