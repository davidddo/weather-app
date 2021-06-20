package de.hdm.weatherapp.views;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.slider.Slider;

import de.hdm.weatherapp.R;
import de.hdm.weatherapp.database.entity.CacheEntity;
import de.hdm.weatherapp.database.repository.CacheRepository;
import de.hdm.weatherapp.interfaces.Location;
import de.hdm.weatherapp.interfaces.common.WeatherItem;
import de.hdm.weatherapp.interfaces.current.CurrentWeatherResponse;
import de.hdm.weatherapp.interfaces.forecast.day.DayForecastResponse;
import de.hdm.weatherapp.interfaces.forecast.day.HourlyWeather;
import de.hdm.weatherapp.interfaces.forecast.week.WeekForecastResponse;
import de.hdm.weatherapp.utils.ApiClient;
import de.hdm.weatherapp.utils.Utils;

public class WeatherView extends FrameLayout {
    private final Resources resources;
    private final Handler handler;
    private final CacheRepository cacheRepository;

    private final LinearLayout weatherContainer;

    private final TextView titleView;
    private final TextView subtitleView;
    private final TextView temperatureView;

    private final Chip feelsLikeChip;
    private final Chip windSpeedChip;
    private final Slider timeSlider;

    private final WeatherIconView weatherIconView;
    private final WeekForecastView weekForecastView;

    private Location location;

    private final  Runnable weatherUpdater = new Runnable() {
        @Override
        public void run() {
            loadWeather(location);
            handler.postDelayed(weatherUpdater, 600000);
        }
    };

    public WeatherView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        inflate(context, R.layout.weather, this);

        handler = new Handler();
        resources = getResources();
        cacheRepository = new CacheRepository(context);

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

    public void bindLocation(Location location) {
        if (this.location == location) return;
        this.location = location;

        loadWeather(location);
    }

    private void loadWeather(Location location) {
        CacheEntity cache = cacheRepository.getByLocation(location);
        if (cache != null && cacheRepository.checkIfCacheIsValid(cache)) {
            updateWeatherView(cache.currentWeather);
            initTimeSlider(cache.dayForecast);
            setWeekForecast(cache.weekForecast);
            return;
        }

        double latitude = location.latitude;
        double longitude = location.longitude;

        loadCurrentWeather(latitude, longitude);
        loadHourlyForecast(latitude, longitude);
        loadWeekForecast(latitude, longitude);
    }

    private void loadCurrentWeather(double latitude, double longitude) {
        ApiClient.getClient().loadCurrentWeather(latitude, longitude, response -> {
            updateWeatherView(response);
            cacheRepository.insertOrUpdate(latitude, longitude, response);
        });
    }

    private void loadHourlyForecast(double latitude, double longitude) {
        ApiClient.getClient().loadDayForecast(latitude, longitude, response -> {
            initTimeSlider(response);
            cacheRepository.insertOrUpdate(latitude, longitude, response);
        });
    }

    private void loadWeekForecast(double latitude, double longitude) {
        ApiClient.getClient().loadWeekForecast(latitude, longitude, response -> {
            setWeekForecast(response);
            cacheRepository.insertOrUpdate(latitude, longitude, response);
        });
    }

    private void initTimeSlider(DayForecastResponse response) {
        timeSlider.setLabelFormatter(value -> {
            final long dateTime = response.hourly.get((int) value).dateTime;
            return resources.getString(R.string.slider_label, formatTime(dateTime, "HH:mm"));
        });
        timeSlider.addOnChangeListener((slider, value, fromUser) -> updateWeatherView(response.hourly.get((int) value)));
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
        this.titleView.setText(resources.getString(R.string.title, city, country));
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
