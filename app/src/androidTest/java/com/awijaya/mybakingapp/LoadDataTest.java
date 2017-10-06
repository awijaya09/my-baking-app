package com.awijaya.mybakingapp;

/**
 * Created by awijaya on 10/1/17.
 */
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LoadDataTest {
    @Rule public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    private CountingIdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource(){
        mIdlingResource = mainActivityActivityTestRule.getActivity().getEspressoIdlingResourceForMainActivity();
        Espresso.registerIdlingResources(mIdlingResource);

    }

    @Test
    public void checkIfListIsDisplayed(){
        onView(withId(R.id.rv_main_list)).check(ViewAssertions.matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource(){
        Espresso.unregisterIdlingResources(mIdlingResource);
    }


}
