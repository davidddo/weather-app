package de.hdm.weatherapp.fragments;

import android.os.Bundle;
import android.util.Log;
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
import de.hdm.weatherapp.database.FavoriteDatabase;
import de.hdm.weatherapp.database.entity.CityEntity;
import de.hdm.weatherapp.database.entity.FavoriteEntity;
import de.hdm.weatherapp.models.forecast.day.DayForecastResponse;
import de.hdm.weatherapp.models.forecast.week.WeekForecastResponse;
import de.hdm.weatherapp.utils.ApiClient;
import de.hdm.weatherapp.views.WeatherView;

public class SearchResultFragment extends Fragment {

    FavoriteDatabase favoriteDatabase;
    CityDatabase cityDatabase;
    CityEntity city;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);

        favoriteDatabase = AppDatabase.instance(requireContext()).getFavoriteDatabase();

        int cityId = getArguments().getInt("cityId");
        cityDatabase = AppDatabase.instance(requireContext()).getCityDatabase();
        city = cityDatabase.cityDao().getById(cityId);

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
            if(favoriteDatabase.favoriteDao().getById(city.id) == null){

                FavoriteEntity favoriteEntity = new FavoriteEntity(city.id, 0, 0);

                Log.e("-SA-","inserting city");
                favoriteDatabase.favoriteDao().insert(favoriteEntity);
            }else{
                Log.e("-SA-","city already exists");
            }
            //TODO: Save city to favourites
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
