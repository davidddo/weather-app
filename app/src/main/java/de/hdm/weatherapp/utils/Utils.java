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
}
