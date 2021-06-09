package de.hdm.weatherapp.utils;

import de.hdm.weatherapp.R;

public class WeatherUtils {
    public static int getWeatherIcon(int code) {
        if (code / 100 == 2) {
            return R.drawable.ic_few_clouds;
        } else if (code / 100 == 3) {
            return R.drawable.ic_rainy_weather;
        } else if (code / 100 == 5) {
            return R.drawable.ic_rainy_weather;
        } else if (code / 100 == 6) {
            return R.drawable.ic_snow_weather;
        } else if (code / 100 == 7) {
            return R.drawable.ic_unknown;
        } else if (code == 800) {
            return R.drawable.ic_clear_day;
        } else if (code == 801) {
            return R.drawable.ic_few_clouds;
        } else if (code == 803) {
            return R.drawable.ic_broken_clouds;
        } else if (code / 100 == 8) {
            return R.drawable.ic_cloudy_weather;
        }

        return R.drawable.ic_unknown;
    }
}
