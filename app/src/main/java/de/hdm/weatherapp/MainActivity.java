package de.hdm.weatherapp;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import de.hdm.weatherapp.database.AppDatabase;
import de.hdm.weatherapp.utils.Utils;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();
        AppDatabase.getInstance(context).initCityDatabase(context);

        initBottomNavigationBar();
    }

    private void initBottomNavigationBar() {
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_favourites, R.id.navigation_search)
                .build();

        NavController navController = Utils.getNavigationController(getSupportFragmentManager());
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigation, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Utils.getNavigationController(getSupportFragmentManager());
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}
