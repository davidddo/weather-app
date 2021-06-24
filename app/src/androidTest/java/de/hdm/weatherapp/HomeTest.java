package de.hdm.weatherapp;


import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.SystemClock;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import de.hdm.weatherapp.database.AppDatabase;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static de.hdm.weatherapp.Helper.childAtPosition;
import static de.hdm.weatherapp.Helper.textViewHasValue;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class HomeTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION",
                    "android.permission.ACCESS_COARSE_LOCATION");


    @Before
    public void init(){


        LocationManager lm = (LocationManager) mActivityTestRule.getActivity()
                .getSystemService(Context.LOCATION_SERVICE);

        String provider = lm.getBestProvider(new Criteria(), false);

        lm.addTestProvider(provider, false, false, false, false, true, true, true, Criteria.POWER_HIGH, Criteria.ACCURACY_FINE);

        Location location = new Location(provider);
        location.setLatitude(44.54536188596672);
        location.setLongitude(-64.72642469333522);
        location.setTime(new Date().getTime());
        location.setElapsedRealtimeNanos(System.nanoTime());
        location.setAccuracy(100.0f);

        lm.setTestProviderEnabled(provider, true);

        lm.setTestProviderStatus(provider,
                LocationProvider.AVAILABLE,
                null,System.currentTimeMillis());

        lm.setTestProviderLocation(provider, location);


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void homeTest() {


        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.navigation_home),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottom_navigation),
                                        0),
                                0),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.title),
                        withParent(allOf(withId(R.id.weather_container),
                                withParent(withId(R.id.weather_view)))),
                        isDisplayed()));
        textView4.check(matches(withText("New Germany, CA")));

        onView(withId(R.id.temperature))
                .check(matches(textViewHasValue()));

    }

}
