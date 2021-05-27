package de.hdm.weatherapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import org.jetbrains.annotations.NotNull;

import de.hdm.weatherapp.R;
import de.hdm.weatherapp.database.AppDatabase;
import de.hdm.weatherapp.database.CityDatabase;
import de.hdm.weatherapp.database.entity.CityEntity;
import de.hdm.weatherapp.views.WeatherView;

public class SearchResultFragment extends Fragment {


    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);

        int cityId = getArguments().getInt("cityId");
        CityDatabase cityDatabase = AppDatabase.instance(requireContext()).getCityDatabase();
        CityEntity city = cityDatabase.cityDao().getById(cityId);

        WeatherView weatherView = view.findViewById(R.id.weather_view);
        weatherView.bindLocation(city.coord.lat, city.coord.lon);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_result_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == R.id.action_favorite) {
            //TODO: Save city to favourites
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
