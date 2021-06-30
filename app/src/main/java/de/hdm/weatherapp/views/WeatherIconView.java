package de.hdm.weatherapp.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;

import de.hdm.weatherapp.utils.Utils;

public class WeatherIconView extends AppCompatImageView {

    public WeatherIconView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setWeatherId(int id) {
        final int iconCode = Utils.getWeatherIcon(id);
        Glide.with(this).load(iconCode).into(this);
    }
}
