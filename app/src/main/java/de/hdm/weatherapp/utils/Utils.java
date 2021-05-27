package de.hdm.weatherapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.RequiresPermission;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;

import de.hdm.weatherapp.R;

public class Utils {

    public static String getJsonFromAssets(Context context, String fileName) {
        try {
            InputStream inputStream = context.getAssets().open(fileName);

            int size = inputStream.available();
            byte[] buffer = new byte[size];

            inputStream.read(buffer);
            inputStream.close();

            return new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long getHourDateTime(long time){

        time = time - (time % 3600);

        return time;
    }

    public static NavController getNavigationController(FragmentManager fragmentManager) {
        NavHostFragment hostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.navigation_host);
        return hostFragment.getNavController();
    }
}
