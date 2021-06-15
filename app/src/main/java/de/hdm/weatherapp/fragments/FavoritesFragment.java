package de.hdm.weatherapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.paging.LoadState;
import androidx.paging.LoadStateAdapter;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingDataAdapter;
import androidx.paging.PagingLiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdm.weatherapp.R;
import de.hdm.weatherapp.database.AppDatabase;
import de.hdm.weatherapp.database.CityDatabase;
import de.hdm.weatherapp.database.FavoriteDatabase;
import de.hdm.weatherapp.database.dao.CityDao;
import de.hdm.weatherapp.database.entity.CityEntity;
import de.hdm.weatherapp.database.entity.FavoriteEntity;
import de.hdm.weatherapp.models.cities.City;
import de.hdm.weatherapp.models.forecast.week.DailyWeather;
import de.hdm.weatherapp.models.forecast.week.WeekForecastResponse;
import de.hdm.weatherapp.utils.ApiClient;
import de.hdm.weatherapp.views.WeatherIconView;
import de.hdm.weatherapp.views.WeekForecastView;
import kotlinx.coroutines.CoroutineScope;

public class FavoritesFragment extends Fragment {

    FavoriteDatabase favoriteDatabase;
    CityDatabase cityDatabase;
    List<FavoriteEntity> displayedMovies;

    public FavoritesFragment() {
    }

    public static FavoritesFragment newInstance() {
        FavoritesFragment fragment = new FavoritesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("-SA-","fav created");
        favoriteDatabase = AppDatabase.instance(requireContext()).getFavoriteDatabase();
        cityDatabase = AppDatabase.instance(requireContext()).getCityDatabase();
        displayedMovies = favoriteDatabase.favoriteDao().getAll();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.favorites_recyler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.requireContext()));

        recyclerView.setHasFixedSize(true);

        MyItemRecyclerViewAdapter  adapter = new MyItemRecyclerViewAdapter(displayedMovies);


        recyclerView.setAdapter(adapter);


        return view;


    }

    public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

        private final List<FavoriteEntity> mValues;


        public MyItemRecyclerViewAdapter(List<FavoriteEntity> mValues) {
            this.mValues = mValues;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item, parent, false);

            ViewHolder viewHolder = new ViewHolder(view);

            return viewHolder;
        }



        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            FavoriteEntity favorite = mValues.get(position);
            if (favorite != null) {


                holder.mView.setOnClickListener(view -> {
                    Bundle args = new Bundle();
                    args.putInt("cityId", favorite.cityId);
                    Navigation.findNavController(view).navigate(R.id.action_navigation_favourites_to_navigation_search_result, args);
                });
            }

            holder.favoriteData = mValues.get(position);
            Log.e("-SA-", Integer.toString(mValues.get(position).cityId));
            CityEntity cityEntity = cityDatabase.cityDao().getById(mValues.get(position).cityId);
            /*if(cityEntity == null){
                //what if city doesnt exist
                Log.e("-SA-", "remove city");
                mValues.remove(position);
                return;
            }else{

            }*/

            new Thread(new Runnable() {
                @Override
                public void run() {


                    ApiClient.getClient().loadWeekForecast(cityEntity.coord.lat, cityEntity.coord.lon, new ApiClient.ResponseListener<WeekForecastResponse>() {
                        @Override
                        public void onResponse(WeekForecastResponse response) {

                            String minTemp = String.format("%s°", (int) response.daily.get(0).temp.min);
                            String maxTemp = String.format("%s°", (int) response.daily.get(0).temp.max);

                            holder.name.setText(cityEntity.name);
                            holder.country.setText(cityEntity.country);

                            holder.minTempView.setText(minTemp);
                            holder.maxTempView.setText(maxTemp);

                            holder.weatherIconView.setWeatherId(response.daily.get(0).weather.get(0).id);
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Log.e("Oh noo couldn't save favorite.", throwable.getMessage());
                        }
                    });

                }
            }).start();





        }

        @Override
        public int getItemCount() {

            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public final TextView name;
            public final TextView country;
            public final TextView minTempView;
            public final TextView maxTempView;
            public final WeatherIconView weatherIconView;

            public FavoriteEntity favoriteData;

            public View mView;

            public ViewHolder(View itemView) {
                super(itemView);

                mView = itemView;

                this.name = itemView.findViewById(R.id.name);
                this.country = itemView.findViewById(R.id.country);
                this.minTempView = itemView.findViewById(R.id.min_temp);
                this.maxTempView = itemView.findViewById(R.id.max_temp);
                this.weatherIconView = itemView.findViewById(R.id.weather_icon);

                name.setOnClickListener(this);
                country.setOnClickListener(this);

                mView.setOnClickListener(this);
            }


            public void removeAt(int position) {
                mValues.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mValues.size());
            }

            @Override
            public void onClick(View v) {

            }
        }
    }

}