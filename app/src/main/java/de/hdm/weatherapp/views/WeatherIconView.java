package de.hdm.weatherapp.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import de.hdm.weatherapp.R;
import de.hdm.weatherapp.models.common.WeatherItem;


public class WeatherIconView extends AppCompatImageView {

    public WeatherIconView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setWeather(WeatherItem weather) {
        this.setBackgroundResource(R.drawable.icon_cloudy_sunny);
    }
}
