package de.hdm.weatherapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        WeathercardFragment weathercardFragment = new WeathercardFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.currentWeatherDetails,weathercardFragment).commit();
    }
}