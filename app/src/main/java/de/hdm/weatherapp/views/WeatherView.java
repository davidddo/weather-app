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

    private final LinearLayout weatherContainer;

    private final TextView titleView;
    private final TextView subtitleView;
    private final TextView feelsLikeView;
    private final TextView windSpeedView;

    private final WeatherIconView weatherIconView;
    private final TextView temperatureView;

    private Slider timeSlider;

    private DayForecastResponse dayForecastResponse;

    private final WeekForecastView weekForecastView;

    public WeatherView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        inflate(context, R.layout.weather, this);

        apiService = ApiClient.getClient().create(ApiService.class);

        weatherContainer = this.findViewById(R.id.weather_container);
        weatherContainer.setVisibility(View.GONE);

        titleView = this.findViewById(R.id.title);
        subtitleView = this.findViewById(R.id.subtitle);
        feelsLikeView = this.findViewById(R.id.feels_like);
        windSpeedView = this.findViewById(R.id.wind_speed);

        weatherIconView = this.findViewById(R.id.weather_icon);
        temperatureView = this.findViewById(R.id.temperature);

        weekForecastView = this.findViewById(R.id.week_forecast);
        timeSlider = this.findViewById(R.id.time_slider);


        LabelFormatter formatter = new LabelFormatter() {
            @NonNull
            @NotNull
            @Override
            public String getFormattedValue(float value) {
                long hourtime;

                Log.e("-SA-",Float.toString(value));
                hourtime = (long) dayForecastResponse.hourly.get((int) value).dateTime;
                Log.e("-SA-",Long.toString(hourtime));


                Format formatter = new SimpleDateFormat("HH:mm");


                Date date = new Date(hourtime*1000);

                return formatter.format(date);
            }
        };

        timeSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull @NotNull Slider slider, float value, boolean fromUser) {
                changeCurrentWeather(dayForecastResponse.hourly.get((int)value));
            }
        });

        timeSlider.setLabelFormatter(formatter);
    }

    public void bindLocation(double latitude, double longitude) {
        loadCurrentWeather(latitude, longitude);
        loadWeekForecast(latitude, longitude);
        loadHourlyForecast(latitude,longitude);
    }

    private void loadCurrentWeather(double latitude, double longitude) {
        Call<CurrentWeatherResponse> call = apiService.getCurrentWeather(latitude, longitude, "metric", "de", ApiClient.API_KEY);
        call.enqueue(new Callback<CurrentWeatherResponse>() {
            @Override
            public void onResponse(@NotNull Call<CurrentWeatherResponse> call, @NotNull Response<CurrentWeatherResponse> response) {
                CurrentWeatherResponse weatherResponse = response.body();
                Log.e("-SA-",weatherResponse.toString());
                if (weatherResponse != null) setCurrentWeather(weatherResponse);
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
                Log.e("-SA-",forecastResponse.toString());
                if (forecastResponse != null)  setDayForecast(forecastResponse);
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
                if (forecastResponse != null) setWeekForecast (forecastResponse);
            }

            @Override
            public void onFailure(@NotNull Call<WeekForecastResponse> call, @NotNull Throwable t) {
                Log.e("Oh noo WeeklyForecast.", t.getMessage());
            }
        });
    }

    private void changeCurrentWeather(HourlyWeather response) {
        Resources resources = getResources();


        final WeatherItem weather = response.weather.get(0);
        final String subtitle = weather.description;
        final String temperature = String.valueOf((int)response.temp)+"°C";
        final String feelsLike = Double.toString(response.feelsLike);
        final String windSpeed = Double.toString(response.windSpeed);

        this.subtitleView.setText(subtitle);
        this.feelsLikeView.setText(feelsLike);
        this.windSpeedView.setText(windSpeed);
        this.temperatureView.setText(temperature);
        this.weatherIconView.setWeatherId(weather.id);
    }

    private void setCurrentWeather(CurrentWeatherResponse response) {
        Resources resources = getResources();
        final WeatherItem weather = response.weather.get(0);

        final String title = response.name;
        final String subtitle = weather.description;
        final String temperature = String.valueOf((int)response.main.temp)+"°C";
        final String feelsLike = resources.getString(R.string.feels_like, String.valueOf(response.main.feelsLike));
        final String windSpeed = resources.getString(R.string.wind_speed, String.valueOf(response.wind.speed));

        this.titleView.setText(title);
        this.subtitleView.setText(subtitle);
        this.feelsLikeView.setText(feelsLike);
        this.windSpeedView.setText(windSpeed);
        this.temperatureView.setText(temperature);
        this.weatherIconView.setWeatherId(weather.id);

        weatherContainer.setVisibility(View.VISIBLE);
    }

    private void setWeekForecast(WeekForecastResponse response) {
        weekForecastView.setWeekForecast(response);
    }

    private void setDayForecast(DayForecastResponse response) {
        this.dayForecastResponse = response;
    }


}
