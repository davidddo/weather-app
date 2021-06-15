/*package de.hdm.weatherapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.hdm.weatherapp.R;
import de.hdm.weatherapp.database.CityDatabase;
import de.hdm.weatherapp.database.FavoriteDatabase;
import de.hdm.weatherapp.database.entity.CityEntity;
import de.hdm.weatherapp.database.entity.FavoriteEntity;
import de.hdm.weatherapp.models.forecast.week.DailyWeather;
import de.hdm.weatherapp.models.forecast.week.WeekForecastResponse;
import de.hdm.weatherapp.utils.ApiClient;

public class FavoriteView extends RecyclerView {

    private final MyItemRecyclerViewAdapter adapter;
    FavoriteDatabase favoriteDatabase;
    CityDatabase cityDatabase;

    public FavoriteView(@NonNull Context context, @Nullable AttributeSet attrs){
        super(context, attrs);

        adapter = new MyItemRecyclerViewAdapter();

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        setLayoutManager(layoutManager);
        setAdapter(adapter);
        setHasFixedSize(true);
        setNestedScrollingEnabled(false);

    }

    public void setFavoriteView (List<FavoriteEntity> favoriteEntities) {

        adapter.setFavoriteList(favoriteEntities);
    }

    public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

        private List<FavoriteEntity> mValues = new ArrayList<>();
        String cityName = "";
        String countryName = "";
        String minTemp = "";
        String maxTemp = "";

        public void setFavoriteList(List<FavoriteEntity> mValues) {
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

            holder.favoriteData = mValues.get(position);

            CityEntity cityEntity = cityDatabase.cityDao().getById(mValues.get(position).id);
            if(cityEntity == null){
                //what if city doesnt exist
                mValues.remove(position);
                return;
            }else{
                new Thread(new Runnable() {
                    @Override
                    public void run() {


                        ApiClient.getClient().loadWeekForecast(cityEntity.coord.lat, cityEntity.coord.lon, new ApiClient.ResponseListener<WeekForecastResponse>() {
                            @Override
                            public void onResponse(WeekForecastResponse response) {

                                holder.setWeatherForecast(cityEntity, response.daily.get(0));
                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                Log.e("Oh noo couldn't save favorite.", throwable.getMessage());
                            }
                        });

                    }
                }).start();
            }




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

            public void setWeatherForecast(CityEntity cityEntity, DailyWeather daily) {
                String minTemp = String.format("%s°", (int) daily.temp.min);
                String maxTemp = String.format("%s°", (int) daily.temp.max);

                this.name.setText(cityName);
                this.country.setText(countryName);

                this.minTempView.setText(minTemp);
                this.maxTempView.setText(maxTemp);

                this.weatherIconView.setWeatherId(daily.weather.get(0).id);
            }

            @Override
            public void onClick(View v) {





            }


            public void removeAt(int position) {
                mValues.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mValues.size());
            }
        }
    }
}
*/