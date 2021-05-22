package de.hdm.weatherapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import de.hdm.weatherapp.R;
import de.hdm.weatherapp.models.common.WeatherItem;
import de.hdm.weatherapp.models.current.CurrentWeatherResponse;

public class CurrentWeatherView extends MaterialCardView {

    private final TextView titleView;
    private final TextView subtitleView;

    private final WeatherIconView weatherIconView;
    private final TextView temperatureView;

    public CurrentWeatherView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        inflate(context, R.layout.current_weather, this);

        titleView = this.findViewById(R.id.title);
        subtitleView = this.findViewById(R.id.subtitle);

        weatherIconView = this.findViewById(R.id.weather_icon);
        temperatureView = this.findViewById(R.id.temperature);
    }

    public void setWeather(CurrentWeatherResponse currentWeather) {
        final String title = currentWeather.name;

        final WeatherItem weather = currentWeather.weather.get(0);

        final String subtitle = weather.description;
        final String temperature = String.valueOf(currentWeather.main.temp);

        this.titleView.setText(title);
        this.subtitleView.setText(subtitle);
        this.temperatureView.setText(temperature);

        this.weatherIconView.setWeatherId(weather.id);
    }
}
