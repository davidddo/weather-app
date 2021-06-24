package de.hdm.weatherapp;

import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

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
import static org.hamcrest.Matchers.anyOf;


@RunWith(AndroidJUnit4.class)
public class HomeTestWithoutLocation {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void HomeTestWithoutLocation(){
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

        textView4.check(matches(withText("Stuttgart, DE")));

        onView(withId(R.id.temperature))
                .check(matches(textViewHasValue()));
    }
}
