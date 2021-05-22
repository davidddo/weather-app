package de.hdm.weatherapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import de.hdm.weatherapp.fragments.FavoritesFragment;
import de.hdm.weatherapp.fragments.HomeFragment;
import de.hdm.weatherapp.fragments.SearchFragment;


public class MainActivity extends AppCompatActivity {

    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HomeFragment homeFragment = new HomeFragment();
        FavoritesFragment favoritesFragment = new FavoritesFragment();
        SearchFragment searchFragment = new SearchFragment();

        currentFragment = homeFragment;

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, favoritesFragment, "favourites").hide(favoritesFragment)
                .add(R.id.fragment_container, homeFragment, "home")
                .commit();


        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentManager.beginTransaction().hide(currentFragment).show(homeFragment).commit();
                    currentFragment = homeFragment;
                    return true;

                case R.id.navigation_favourites:
                    fragmentManager.beginTransaction().hide(currentFragment).show(favoritesFragment).commit();
                    currentFragment = favoritesFragment;
                    return true;

                case R.id.navigation_search:
                    fragmentManager.beginTransaction().hide(currentFragment).show(searchFragment).commit();
                    currentFragment = searchFragment;
                    return true;

                default:
                    return false;
            }
        });
    }
}
