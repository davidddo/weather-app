package de.hdm.weatherapp.views;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;

import org.jetbrains.annotations.NotNull;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import de.hdm.weatherapp.R;
import de.hdm.weatherapp.models.common.WeatherItem;
import de.hdm.weatherapp.models.current.CurrentWeatherResponse;
import de.hdm.weatherapp.models.forecast.day.DayForecastResponse;
import de.hdm.weatherapp.models.forecast.day.HourlyWeather;
import de.hdm.weatherapp.models.forecast.week.WeekForecastResponse;
import de.hdm.weatherapp.utils.ApiClient;
import de.hdm.weatherapp.utils.ApiService;
import de.hdm.weatherapp.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherView extends FrameLayout {
    private final ApiService apiService;
    private final Resources resources;

    private final LinearLayout weatherContainer;

    private final TextView titleView;
    private final TextView subtitleView;
    private final TextView feelsLikeView;
    private final TextView windSpeedView;
    private final TextView temperatureView;

    private final WeatherIconView weatherIconView;
    private final WeekForecastView weekForecastView;

    private DayForecastResponse dayForecastResponse;

    public WeatherView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        inflate(context, R.layout.weather, this);

        apiService = ApiClient.getClient().create(ApiService.class);
        resources = getResources();

        weatherContainer = this.findViewById(R.id.weather_container);

        titleView = this.findViewById(R.id.title);
        subtitleView = this.findViewById(R.id.subtitle);
        feelsLikeView = this.findViewById(R.id.feels_like);
        windSpeedView = this.findViewById(R.id.wind_speed);
        weatherIconView = this.findViewById(R.id.weather_icon);
        temperatureView = this.findViewById(R.id.temperature);
        weekForecastView = this.findViewById(R.id.week_forecast);

        Slider timeSlider = this.findViewById(R.id.time_slider);
        LabelFormatter labelFormatter = value -> {
            final long dateTime = dayForecastResponse.hourly.get((int) value).dateTime;
            String formattedTime = new SimpleDateFormat("HH:mm").format( new Date(dateTime * 1000L));

            return String.format("%s Uhr", formattedTime);
        };

        timeSlider.setLabelFormatter(labelFormatter);
        timeSlider.addOnChangeListener((slider, value, fromUser) -> {
            updateWeatherView(dayForecastResponse.hourly.get((int) value));
        });
    }

    public void bindLocation(double latitude, double longitude) {
        loadWeather(latitude, longitude);
    }

    private void loadWeather(double latitude, double longitude) {
        loadCurrentWeather(latitude, longitude);
        loadWeekForecast(latitude, longitude);
        loadHourlyForecast(latitude, longitude);
    }

    private void loadCurrentWeather(double latitude, double longitude) {
        Call<CurrentWeatherResponse> call = apiService.getCurrentWeather(latitude, longitude, "metric", "de", ApiClient.API_KEY);
        call.enqueue(new Callback<CurrentWeatherResponse>() {
            @Override
            public void onResponse(@NotNull Call<CurrentWeatherResponse> call, @NotNull Response<CurrentWeatherResponse> response) {
                CurrentWeatherResponse weatherResponse = response.body();
                if (weatherResponse != null) updateWeatherView(weatherResponse);
            }

            @Override
            public void onFailure(@NotNull Call<CurrentWeatherResponse> call, Throwable t) {
                Log.e("Oh noo CurrentForecast.", t.getMessage());
            }
        });
    }

    private void loadHourlyForecast(double latitude, double longitude) {
        Call<DayForecastResponse> call = apiService.getDayForecast(latitude, longitude, "metric", "de", "current,daily,minutely,alerts", ApiClient.API_KEY);
        call.enqueue(new Callback<DayForecastResponse>() {
            @Override
            public void onResponse(@NotNull Call<DayForecastResponse> call, @NotNull Response<DayForecastResponse> response) {
                DayForecastResponse forecastResponse = response.body();
                if (forecastResponse != null) dayForecastResponse = forecastResponse;
            }

            @Override
            public void onFailure(@NotNull Call<DayForecastResponse> call, @NotNull Throwable t) {
                Log.e("Oh noo HourlyForecast.", t.getMessage());
            }
        });
    }

    private void loadWeekForecast(double latitude, double longitude) {
        Call<WeekForecastResponse> call = apiService.getWeekForecast(latitude, longitude, "metric", "de", "current,minutely,hourly,alerts", ApiClient.API_KEY);
        call.enqueue(new Callback<WeekForecastResponse>() {
            @Override
            public void onResponse(@NotNull Call<WeekForecastResponse> call, @NotNull Response<WeekForecastResponse> response) {
                WeekForecastResponse forecastResponse = response.body();
                if (forecastResponse != null) setWeekForecast(forecastResponse);
            }

            @Override
            public void onFailure(@NotNull Call<WeekForecastResponse> call, @NotNull Throwable t) {
                Log.e("Oh noo WeeklyForecast.", t.getMessage());
            }
        });
    }

    private void updateWeatherView(CurrentWeatherResponse response) {
        final WeatherItem weather = response.weather.get(0);

        updateTitle(response.name);
        updteSubtitle(weather.description);
        updateTemperature(response.main.temp);
        updateWeatherDetails(response.main.feelsLike, response.wind.speed);
        updateWeatherIcon(weather.id);
    }

    private void updateWeatherView(HourlyWeather response) {
        WeatherItem weather = response.weather.get(0);

        updteSubtitle(weather.description);
        updateWeatherIcon(weather.id);
        updateTemperature(response.temp);
        updateWeatherDetails(response.feelsLike, response.windSpeed);
    }

    private void updateTitle(String title) {
        this.titleView.setText(title);
    }

    private void updteSubtitle(String subtitle) {
        this.subtitleView.setText(subtitle);
    }

    private void updateWeatherIcon(int weatherId) {
        this.weatherIconView.setWeatherId(weatherId);
    }

    private void updateTemperature(double temperature) {
        this.temperatureView.setText(resources.getString(R.string.temperature, String.valueOf(temperature)));
    }

    private void updateWeatherDetails(double feelsLike, double windSpeed) {
        this.feelsLikeView.setText(resources.getString(R.string.feels_like, String.valueOf(feelsLike)));
        this.windSpeedView.setText(resources.getString(R.string.wind_speed, String.valueOf(windSpeed)));
    }

    private void setWeekForecast(WeekForecastResponse response) {
        weekForecastView.setWeekForecast(response);
    }
}
