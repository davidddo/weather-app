package de.hdm.weatherapp;

import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.hdm.weatherapp.database.AppDatabase;

import static de.hdm.weatherapp.Helper.*;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class FavouriteTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule grantPermissionRule = GrantPermissionRule.grant(
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION");

    @Before
    public void init() {
        AppDatabase.getInstance(activityTestRule.getActivity()).cityDao().reset();
    }

    @Test
    public void favouriteTest() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.navigation_search)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.search_recycler_view)).perform(actionOnItemAtPosition(2, click()));
        onView(withId(R.id.action_favourite)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.navigation_favourites)).perform(click());
        onView(withId(R.id.favorites_recyler_view)).check(matches(isDisplayed()));

        onView(withId(R.id.name)).check(matches(withText("Taglag")));
        onView(withId(R.id.country)).check(matches(withText("IR")));

        onView(withId(R.id.favorites_recyler_view)).perform(actionOnItemAtPosition(0, click()));
        onView(withId(R.id.title)).check(matches(withText("Taglag, IR")));

        onView(withId(R.id.weather_view)).check(matches(isDisplayed()));
        onView(withId(R.id.temperature)).check(matches(textViewHasValue()));

        onView(withId(R.id.action_favourite)).perform(click());
        onView(withId(R.id.navigation_favourites)).perform(click());
        onView(withId(R.id.favorites_recyler_view)).check(RecyclerViewItemCountAssertion.withItemCount(0));
    }
}
