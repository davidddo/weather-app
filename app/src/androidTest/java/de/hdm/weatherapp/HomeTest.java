package de.hdm.weatherapp;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;

import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static de.hdm.weatherapp.Helper.textViewHasValue;

@RunWith(AndroidJUnit4.class)
public class HomeTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule grantPermissionRule = GrantPermissionRule.grant(
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION");


    @Before
    public void init() {
        LocationManager locationManager = (LocationManager) activityTestRule.getActivity().getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), false);

        locationManager.addTestProvider(provider,
                false,
                false,
                false,
                false,
                true,
                true,
                true,
                Criteria.POWER_HIGH,
                Criteria.ACCURACY_FINE);

        Location location = new Location(provider);
        location.setLatitude(44.54536188596672);
        location.setLongitude(-64.72642469333522);
        location.setTime(new Date().getTime());
        location.setElapsedRealtimeNanos(System.nanoTime());
        location.setAccuracy(100.0f);

        locationManager.setTestProviderEnabled(provider, true);
        locationManager.setTestProviderStatus(provider, LocationProvider.AVAILABLE, null, System.currentTimeMillis());
        locationManager.setTestProviderLocation(provider, location);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void homeTest() {
        onView(withId(R.id.navigation_home)).perform(click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.title)).check(matches(withText("New Germany, CA")));
        onView(withId(R.id.temperature)).check(matches(textViewHasValue()));
    }
}
