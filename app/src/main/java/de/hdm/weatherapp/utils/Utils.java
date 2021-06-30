package de.hdm.weatherapp.utils;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdm.weatherapp.R;

public class Utils {

    public static String getJsonFromAssets(Context context, String fileName) {
        try {
            InputStream inputStream = context.getAssets().open(fileName);

            int size = inputStream.available();
            byte[] buffer = new byte[size];

            inputStream.read(buffer);
            inputStream.close();

            return new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String formatDateTime(long dateTime, String pattern) {
        return new SimpleDateFormat(pattern, Locale.GERMANY).format(new Date(dateTime * 1000L));
    }

    public static NavController getNavigationController(FragmentManager fragmentManager) {
        NavHostFragment hostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.navigation_host);
        return hostFragment.getNavController();
    }

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
