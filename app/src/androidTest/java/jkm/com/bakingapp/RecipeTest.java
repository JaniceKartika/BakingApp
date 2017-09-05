package jkm.com.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import jkm.com.bakingapp.test.RecipeIdlingResource;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RecipeTest {
    private RecipeIdlingResource recipeIdlingResource;

    @Rule
    public final ActivityTestRule<RecipeActivity> rule = new ActivityTestRule<>(RecipeActivity.class, true, true);

    @Before
    public void registerIdlingResource() {
        recipeIdlingResource = new RecipeIdlingResource(rule.getActivity());
        Espresso.registerIdlingResources(recipeIdlingResource);
    }

    @After
    public void unregisterIdlingResource() {
        Espresso.unregisterIdlingResources(recipeIdlingResource);
    }

    @Test
    public void testRecipeMainList() {
        onView(withId(R.id.rv_recipe_list)).check(matches(new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                if (!(item instanceof RecyclerView)) return false;

                RecyclerView recyclerView = (RecyclerView) item;
                Assert.assertTrue(recyclerView.getChildCount() > 0);
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("RecyclerView should not be empty");
            }
        }));

        onView(withId(R.id.rv_recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.iv_recipe_detail)).check(matches(isDisplayed()));
    }
}
