package com.awijaya.mybakingapp;

/**
 * Created by awijaya on 10/1/17.
 */
import android.content.ComponentName;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.StringStartsWith.startsWith;

@RunWith(AndroidJUnit4.class)
public class LoadDataTest {
    @Rule public IntentsTestRule<MainActivity> mainActivityActivityTestRule
            = new IntentsTestRule<>(MainActivity.class);

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

    @Test
    public void checkIfIntentIsCorrect() {
        onView(withId(R.id.rv_main_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(hasComponent(new ComponentName(getTargetContext(), RecipeDetailActivity.class)));
        onView(withId(R.id.rv_detail_list)).check(ViewAssertions.matches(isDisplayed()));
    }


    @After
    public void unregisterIdlingResource(){
        Espresso.unregisterIdlingResources(mIdlingResource);
    }


}
