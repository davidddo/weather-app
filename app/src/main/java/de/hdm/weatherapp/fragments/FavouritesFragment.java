package de.hdm.weatherapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import de.hdm.weatherapp.R;
import de.hdm.weatherapp.database.entity.CacheEntity;
import de.hdm.weatherapp.database.entity.CityEntity;
import de.hdm.weatherapp.database.repository.CacheRepository;
import de.hdm.weatherapp.database.repository.CityRepository;
import de.hdm.weatherapp.interfaces.Location;
import de.hdm.weatherapp.interfaces.current.CurrentWeatherResponse;
import de.hdm.weatherapp.models.FavouritesViewModel;
import de.hdm.weatherapp.utils.ApiClient;
import de.hdm.weatherapp.utils.AppExecutors;
import de.hdm.weatherapp.views.WeatherIconView;

public class FavouritesFragment extends Fragment {
    private CacheRepository cacheRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cacheRepository = new CacheRepository(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.favorites_recyler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.requireContext()));
        FavouritesAdapter adapter = new FavouritesAdapter();
        recyclerView.setAdapter(adapter);

        CityRepository repository = new CityRepository(requireContext());
        FavouritesViewModel.Factory factory = new FavouritesViewModel.Factory(repository);

        FavouritesViewModel model = new ViewModelProvider(this, factory).get(FavouritesViewModel.class);
        model.favourites.observe(getViewLifecycleOwner(), adapter::submitList);

        return view;
    }

    public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder> {
        private List<CityEntity> favourites = new ArrayList<>();

        public void submitList(List<CityEntity> favourites) {
            this.favourites = favourites;
            System.out.println(this.favourites.size());
            notifyDataSetChanged();
        }

        @NotNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NotNull final ViewHolder holder, int position) {
            CityEntity city = favourites.get(position);
            if (city != null) {
                Location location = new Location(city.coord.lat, city.coord.lon);
                CacheEntity cache = cacheRepository.getByLocation(location);
                if (cache != null && cacheRepository.checkIfCacheIsValid(cache)) {
                    holder.bind(city, cache.currentWeather);
                } else {
                    AppExecutors.getInstance().networkIO().execute(
                            () -> ApiClient.getClient().loadCurrentWeather(city.coord.lat, city.coord.lon,
                                    response -> holder.bind(city, response)));
                }

                holder.view.setOnClickListener(view -> {
                    Bundle args = new Bundle();
                    args.putInt("cityId", city.id);
                    Navigation.findNavController(view).navigate(R.id.action_navigation_favourites_to_navigation_search_result, args);
                });
            }
        }

        @Override
        public int getItemCount() {
            return favourites.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View view;
            public final TextView name;
            public final TextView country;
            public final TextView minTempView;
            public final TextView maxTempView;
            public final WeatherIconView weatherIconView;

            public ViewHolder(View view) {
                super(view);

                this.view = view;
                this.name = itemView.findViewById(R.id.name);
                this.country = itemView.findViewById(R.id.country);
                this.minTempView = itemView.findViewById(R.id.min_temp);
                this.maxTempView = itemView.findViewById(R.id.max_temp);
                this.weatherIconView = itemView.findViewById(R.id.weather_icon);
            }

            public void bind(CityEntity city, CurrentWeatherResponse response) {
                name.setText(city.name);
                country.setText(city.country);

                minTempView.setText(String.format("%s°", (int) response.main.tempMin));
                maxTempView.setText(String.format("%s°", (int) response.main.tempMax));
                weatherIconView.setWeatherId(response.weather.get(0).id);
            }
        }
    }
}