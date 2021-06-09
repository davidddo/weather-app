package de.hdm.weatherapp.views;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.slider.Slider;

import de.hdm.weatherapp.R;
import de.hdm.weatherapp.models.common.WeatherItem;
import de.hdm.weatherapp.models.current.CurrentWeatherResponse;
import de.hdm.weatherapp.models.forecast.day.DayForecastResponse;
import de.hdm.weatherapp.models.forecast.day.HourlyWeather;
import de.hdm.weatherapp.models.forecast.week.WeekForecastResponse;
import de.hdm.weatherapp.utils.ApiClient;
import de.hdm.weatherapp.utils.Utils;

public class WeatherView extends FrameLayout {
    private final Resources resources;
    private final Handler handler;

    private final LinearLayout weatherContainer;

    private final TextView titleView;
    private final TextView subtitleView;
    private final TextView temperatureView;

    private final Chip feelsLikeChip;
    private final Chip windSpeedChip;
    private final Slider timeSlider;

    private final WeatherIconView weatherIconView;
    private final WeekForecastView weekForecastView;

    private final  Runnable weatherUpdater = new Runnable() {
        @Override
        public void run() {
            loadWeather(latitude, longitude);
            handler.postDelayed(weatherUpdater, 10000);
        }
    };

    private double latitude;
    private double longitude;

    public WeatherView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        inflate(context, R.layout.weather, this);

        handler = new Handler();
        resources = getResources();

        weatherContainer = findViewById(R.id.weather_container);
        weatherContainer.setVisibility(View.GONE);

        titleView = findViewById(R.id.title);
        subtitleView = findViewById(R.id.subtitle);

        timeSlider = findViewById(R.id.time_slider);
        feelsLikeChip = findViewById(R.id.feels_like);
        windSpeedChip = findViewById(R.id.wind_speed);

        weatherIconView = findViewById(R.id.weather_icon);
        temperatureView = findViewById(R.id.temperature);
        weekForecastView = findViewById(R.id.week_forecast);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacks(weatherUpdater);
    }

    public void bindLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;

        weatherUpdater.run();
    }

    private void loadWeather(double latitude, double longitude) {
        loadCurrentWeather(latitude, longitude);
        loadWeekForecast(latitude, longitude);
        loadHourlyForecast(latitude, longitude);
    }

    private void loadCurrentWeather(double latitude, double longitude) {
        ApiClient.getClient().loadCurrentWeather(latitude, longitude, new ApiClient.ResponseListener<CurrentWeatherResponse>() {
            @Override
            public void onResponse(CurrentWeatherResponse response) {
                updateWeatherView(response);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("Oh noo CurrentForecast.", throwable.getMessage());
            }
        });
    }

    private void loadHourlyForecast(double latitude, double longitude) {
        ApiClient.getClient().loadDayForecast(latitude, longitude, new ApiClient.ResponseListener<DayForecastResponse>() {
            @Override
            public void onResponse(DayForecastResponse response) {
                initTimeSlider(response);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("Oh noo HourlyForecast.", throwable.getMessage());
            }
        });
    }

    private void loadWeekForecast(double latitude, double longitude) {
        ApiClient.getClient().loadWeekForecast(latitude, longitude, new ApiClient.ResponseListener<WeekForecastResponse>() {
            @Override
            public void onResponse(WeekForecastResponse response) {
                setWeekForecast(response);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("Oh noo WeeklyForecast.", throwable.getMessage());
            }
        });
    }

    private void initTimeSlider(DayForecastResponse response) {
        timeSlider.setLabelFormatter(value -> {
            final long dateTime = response.hourly.get((int) value).dateTime;
            return resources.getString(R.string.slider_label, formatTime(dateTime, "HH:mm"));
        });

        timeSlider.addOnChangeListener((slider, value, fromUser) -> {
            updateWeatherView(response.hourly.get((int) value));
        });
    }

    private void updateWeatherView(CurrentWeatherResponse response) {
        final WeatherItem weather = response.weather.get(0);

        updateTitle(response.name, response.sys.country);
        updateSubtitle(weather.description, response.dt);
        updateTemperature(response.main.temp);
        updateWeatherDetails(response.main.feelsLike, response.wind.speed);
        updateWeatherIcon(weather.id);

        weatherContainer.setVisibility(View.VISIBLE);
    }

    private void updateWeatherView(HourlyWeather response) {
        WeatherItem weather = response.weather.get(0);

        updateSubtitle(weather.description, response.dateTime);
        updateWeatherIcon(weather.id);
        updateTemperature(response.temp);
        updateWeatherDetails(response.feelsLike, response.windSpeed);
    }

    private void updateTitle(String city, String country) {
        this.titleView.setText(resources.getString(R.string.subtitle, city, country));
    }

    private void updateSubtitle(String subtitle, long dateTime) {
        String formattedDateTime = formatTime(dateTime, "EE HH:mm");
        this.subtitleView.setText(resources.getString(R.string.subtitle, formattedDateTime, subtitle));
    }

    private void updateWeatherIcon(int weatherId) {
        this.weatherIconView.setWeatherId(weatherId);
    }

    private void updateTemperature(double temperature) {
        this.temperatureView.setText(resources.getString(R.string.temperature, String.valueOf(temperature)));
    }

    private void updateWeatherDetails(double feelsLike, double windSpeed) {
        this.feelsLikeChip.setText(resources.getString(R.string.feels_like, String.valueOf(feelsLike)));
        this.windSpeedChip.setText(resources.getString(R.string.wind_speed, String.valueOf(windSpeed)));
    }

    private void setWeekForecast(WeekForecastResponse response) {
        weekForecastView.setWeekForecast(response);
    }

    private String formatTime(long dateTime, String pattern) {
        return Utils.formatDateTime(dateTime, pattern);
    }
}
