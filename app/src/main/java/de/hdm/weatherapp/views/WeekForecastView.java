package de.hdm.weatherapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdm.weatherapp.R;
import de.hdm.weatherapp.models.forecast.week.DailyWeather;
import de.hdm.weatherapp.models.forecast.week.WeekForecastResponse;

public class WeekForecastView extends RecyclerView {
    private final WeekForecastViewAdapter adapter;

    public WeekForecastView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        adapter = new WeekForecastViewAdapter();

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        setLayoutManager(layoutManager);
        setAdapter(adapter);
        setHasFixedSize(true);
        setNestedScrollingEnabled(false);
    }

    public void setWeekForecast(WeekForecastResponse forecast) {
        adapter.setDaily(forecast.daily);
    }

    class WeekForecastViewAdapter extends RecyclerView.Adapter<WeatherForecastViewHolder> {
        private List<DailyWeather> daily = new ArrayList<>();

        @Override
        public WeatherForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_item, parent, false);
            return new WeatherForecastViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final WeatherForecastViewHolder holder, int position) {
            holder.setWeatherForecast(daily.get(position));
        }

        public void setDaily(List<DailyWeather> daily) {
            this.daily = daily;
            adapter.notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return daily.size();
        }
    }

    class WeatherForecastViewHolder extends RecyclerView.ViewHolder {
        public final View view;

        public final TextView dayView;
        public final TextView minTempView;
        public final TextView maxTempView;
        public final WeatherIconView weatherIconView;

        public WeatherForecastViewHolder(View view) {
            super(view);

            this.view = view;
            this.dayView = view.findViewById(R.id.day);
            this.minTempView = view.findViewById(R.id.min_temp);
            this.maxTempView = view.findViewById(R.id.max_temp);
            this.weatherIconView = view.findViewById(R.id.weather_icon);
        }

        public void setWeatherForecast(DailyWeather daily) {
            String day = new SimpleDateFormat("E").format(new Date(daily.dateTime * 1000L));
            String minTemp = String.format("%s°", (int) daily.temp.min);
            String maxTemp = String.format("%s°", (int) daily.temp.max);

            this.dayView.setText(day);
            this.minTempView.setText(minTemp);
            this.maxTempView.setText(maxTemp);

            this.weatherIconView.setWeatherId(daily.weather.get(0).id);
        }
    }
}