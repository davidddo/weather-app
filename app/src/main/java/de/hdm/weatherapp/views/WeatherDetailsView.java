package de.hdm.weatherapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdm.weatherapp.R;
import de.hdm.weatherapp.models.currentweather.CurrentWeatherResponse;

public class WeatherDetailsView extends LinearLayout {

    private final TextView humidityView;
    private final TextView windSpeedView;

    public WeatherDetailsView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        inflate(context, R.layout.weather_details, this);

        humidityView = this.findViewById(R.id.humidity);
        windSpeedView = this.findViewById(R.id.wind_speed);
    }

    public void setWeather(CurrentWeatherResponse currentWeather) {
        final String humidity = String.format("Luftfeuchte: %s%%", currentWeather.getMain().getHumidity());
        final String windSpeed = String.format("Wind: %s km/h", currentWeather.getWind().getSpeed());

        this.humidityView.setText(humidity);
        this.windSpeedView.setText(windSpeed);
    }
}
