package de.hdm.weatherapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import de.hdm.weatherapp.R;
import de.hdm.weatherapp.database.entity.CityEntity;
import de.hdm.weatherapp.interfaces.Location;
import de.hdm.weatherapp.models.CityWeatherViewModel;
import de.hdm.weatherapp.views.WeatherView;

public class CityWeatherFragment extends Fragment {
    private CityWeatherViewModel model;
    private MenuItem favouriteItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_weather, container, false);
        WeatherView weatherView = view.findViewById(R.id.weather_view);

        final int cityId = requireArguments().getInt("cityId");
        CityWeatherViewModel.Factory factory = new CityWeatherViewModel.Factory(requireContext(), cityId);

        model = new ViewModelProvider(this, factory).get(CityWeatherViewModel.class);
        model.city.observe(getViewLifecycleOwner(), city -> {
            weatherView.bindLocation(new Location(city.coord.lat, city.coord.lon));
            updateFavouriteIcon(city.saved);
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.city_weather_menu, menu);
        favouriteItem = menu.findItem(R.id.action_favourite);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == R.id.action_favourite) {
            final CityEntity city = model.city.getValue();
            if (city != null) {
                model.updateBookmark(!city.saved);
                updateFavouriteIcon(!city.saved);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateFavouriteIcon(boolean saved) {
        favouriteItem.setIcon(saved ? R.drawable.ic_favorite_24dp : R.drawable.ic_favorite_border_24dp);
    }
}
