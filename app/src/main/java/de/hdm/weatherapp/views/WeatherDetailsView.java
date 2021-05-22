package de.hdm.weatherapp.views;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdm.weatherapp.R;
import de.hdm.weatherapp.models.current.CurrentWeatherResponse;

public class WeatherDetailsView extends LinearLayout {

    private final TextView feelsLikeView;
    private final TextView windSpeedView;

    public WeatherDetailsView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        inflate(context, R.layout.weather_details, this);

        feelsLikeView = this.findViewById(R.id.feels_like);
        windSpeedView = this.findViewById(R.id.wind_speed);
    }

    public void setWeather(CurrentWeatherResponse currentWeather) {
        Resources resources = getResources();

        System.out.println(resources.getString(R.string.feels_like));

        final String feelsLike = resources.getString(R.string.feels_like, String.valueOf(currentWeather.main.feelsLike));
        final String windSpeed = resources.getString(R.string.wind_speed, String.valueOf(currentWeather.main.feelsLike));

        this.feelsLikeView.setText(feelsLike);
        this.windSpeedView.setText(windSpeed);
    }
}
