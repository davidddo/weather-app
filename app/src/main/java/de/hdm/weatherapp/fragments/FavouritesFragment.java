package de.hdm.weatherapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import de.hdm.weatherapp.R;
import de.hdm.weatherapp.database.entity.CityEntity;
import de.hdm.weatherapp.database.repository.CityRepository;
import de.hdm.weatherapp.interfaces.forecast.week.WeekForecastResponse;
import de.hdm.weatherapp.models.FavouritesViewModel;
import de.hdm.weatherapp.utils.ApiClient;
import de.hdm.weatherapp.utils.AppExecutors;
import de.hdm.weatherapp.views.WeatherIconView;

public class FavouritesFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    public static class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder> {
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
                holder.bind(city);

                AppExecutors.getInstance().networkIO().execute(() -> {
                    ApiClient.getClient().loadWeekForecast(city.coord.lat, city.coord.lon, new ApiClient.ResponseListener<WeekForecastResponse>() {
                        @Override
                        public void onResponse(WeekForecastResponse response) {
                            String minTemp = String.format("%s°", (int) response.daily.get(0).temp.min);
                            String maxTemp = String.format("%s°", (int) response.daily.get(0).temp.max);

                            holder.minTempView.setText(minTemp);
                            holder.maxTempView.setText(maxTemp);
                            holder.weatherIconView.setWeatherId(response.daily.get(0).weather.get(0).id);
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Log.e("Oh noo couldn't save favorite.", throwable.getMessage());
                        }
                    });
                });

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

            public void removeAt(int position) {
                favourites.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, favourites.size());
            }

            public void bind(CityEntity city) {
                name.setText(city.name);
                country.setText(city.country);
            }
        }
    }
}