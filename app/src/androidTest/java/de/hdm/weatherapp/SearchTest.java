package de.hdm.weatherapp;

import androidx.test.espresso.ViewInteraction;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static de.hdm.weatherapp.Helper.*;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class SearchTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule grantPermissionRule = GrantPermissionRule.grant(
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION");

    @Test
    public void searchTest() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.navigation_search)).perform(click());
        onView(withId(R.id.search)).perform(click());

        ViewInteraction searchAutoComplete = onView(
                allOf(withClassName(is("android.widget.SearchView$SearchAutoComplete")),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.LinearLayout")),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                0),
                        isDisplayed()));

        searchAutoComplete.perform(replaceText("stuttgart"), closeSoftKeyboard());
        searchAutoComplete.perform(pressImeActionButton());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.search_recycler_view)).check(RecyclerViewItemCountAssertion.withItemCount(7));

        onView(withId(R.id.search_recycler_view)).check(matches(atPosition(0, hasDescendant(withText("Stuttgart")))));
        onView(withId(R.id.search_recycler_view)).check(matches(atPosition(1, hasDescendant(withText("Stuttgart Muehlhausen")))));
        onView(withId(R.id.search_recycler_view)).check(matches(atPosition(2, hasDescendant(withText("Stuttgart Feuerbach")))));
        onView(withId(R.id.search_recycler_view)).check(matches(atPosition(3, hasDescendant(withText("Regierungsbezirk Stuttgart")))));
        onView(withId(R.id.search_recycler_view)).check(matches(atPosition(4, hasDescendant(withText("Stadtkreis Stuttgart")))));
        onView(withId(R.id.search_recycler_view)).check(matches(atPosition(5, hasDescendant(withText("Stuttgart")))));
        onView(withId(R.id.search_recycler_view)).check(matches(atPosition(6, hasDescendant(withText("Stuttgart-Ost")))));

        onView(withId(R.id.search_recycler_view)).perform(actionOnItemAtPosition(0, click()));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.weather_view)).check(matches(isDisplayed()));
        onView(withId(R.id.title)).check(matches(withText("Stuttgart, DE")));
    }
}
