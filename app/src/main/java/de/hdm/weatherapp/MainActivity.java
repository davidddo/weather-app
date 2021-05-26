package de.hdm.weatherapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.hdm.weatherapp.database.AppDatabase;
import de.hdm.weatherapp.database.CityDatabase;
import de.hdm.weatherapp.database.entity.CityEntity;
import de.hdm.weatherapp.utils.Utils;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initNavigation();
        initCities();
    }

    private void initNavigation() {
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_favourites, R.id.navigation_search)
                .build();

        NavController navController = Utils.getNavigationController(getSupportFragmentManager());
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigation, navController);
    }

    private void initCities() {
        CityDatabase database = AppDatabase.instance(getApplicationContext()).getCityDatabase();
        if (database.cityDao().getAll().isEmpty()) {
            System.out.println("INIT");

            Runnable runnable = () -> {
                String json = Utils.getJsonFromAssets(getApplicationContext(),"cities.list.json");
                Type type = new TypeToken<ArrayList<CityEntity>>(){}.getType();

                List<CityEntity> cities = new Gson().fromJson(json, type);
                database.cityDao().insertMany(cities);
            };

            new Thread(runnable).start();
        }
    }
}
