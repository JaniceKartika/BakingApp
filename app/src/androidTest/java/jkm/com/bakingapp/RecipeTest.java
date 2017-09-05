package jkm.com.bakingapp;

import android.support.design.widget.AppBarLayout;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import jkm.com.bakingapp.model.RecipeModel;
import jkm.com.bakingapp.model.StepModel;
import jkm.com.bakingapp.test.RecipeIdlingResource;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

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
    public void testRecipe() {
        onView(withId(R.id.rv_recipe_list)).check(matches(recyclerViewHasChildren()));
        onView(withId(R.id.rv_recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        ArrayList<RecipeModel> recipeModels = rule.getActivity().getRecipeModels();
        StepModel firstStepModel = recipeModels.get(0).getSteps().get(0);

        onView(withId(R.id.iv_recipe_detail)).check(matches(isDisplayed()));
        onView(withId(R.id.recipe_detail_container)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_recipe_name_detail)).check(matches(isDisplayed()));

        if (!rule.getActivity().getResources().getBoolean(R.bool.isTab)) {
            onView(withId(R.id.app_bar_recipe_detail)).perform(collapseAppBarLayout());
        } else {
            onView(withId(R.id.recipe_detail_container)).perform(scrollTo());
        }

        onView(withId(R.id.tv_recipe_ingredients_detail)).perform(CustomScrollActions.nestedScrollTo());
        onView(withId(R.id.rv_recipe_ingredients_detail)).check(matches(recyclerViewHasChildren()));

        onView(withId(R.id.tv_recipe_steps_detail)).perform(CustomScrollActions.nestedScrollTo());
        onView(withId(R.id.rv_recipe_steps_detail)).check(matches(recyclerViewHasChildren()));
        onView(withId(R.id.rv_recipe_steps_detail)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.step_detail_container)).check(matches(isDisplayed()));
        onView(withId(R.id.tab_step_detail)).check(matches(isDisplayed()));
        onView(withId(R.id.iv_arrow_left_step_detail)).check(matches(isDisplayed()));
        onView(withId(R.id.iv_arrow_right_step_detail)).check(matches(isDisplayed()));

        if (firstStepModel.getVideoURL() != null && !firstStepModel.getVideoURL().isEmpty()) {
            onView(allOf(withId(R.id.player_container), isCompletelyDisplayed())).check(matches(isDisplayed()));
        }
        if (firstStepModel.getThumbnailURL() != null && !firstStepModel.getThumbnailURL().isEmpty()) {
            onView(allOf(withId(R.id.iv_each_step), isCompletelyDisplayed())).check(matches(isDisplayed()));
        }
        if (firstStepModel.getDescription() != null && !firstStepModel.getDescription().isEmpty()) {
            onView(allOf(withId(R.id.tv_each_step), isCompletelyDisplayed())).check(matches(isDisplayed()));
        }

        onView(withId(R.id.iv_arrow_right_step_detail)).perform(click());
        onView(withId(R.id.view_pager_step_detail)).check(matches(moveToPage(1)));

        onView(withId(R.id.iv_arrow_left_step_detail)).perform(click());
        onView(withId(R.id.view_pager_step_detail)).check(matches(moveToPage(0)));
    }

    private static TypeSafeMatcher<View> recyclerViewHasChildren() {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                if (!(item instanceof RecyclerView)) return false;

                RecyclerView recyclerView = (RecyclerView) item;
                Assert.assertTrue(recyclerView.getAdapter().getItemCount() > 0);
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("RecyclerView should not be empty");
            }
        };
    }

    private static ViewAction collapseAppBarLayout() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(AppBarLayout.class);
            }

            @Override
            public String getDescription() {
                return "Collapse AppBarLayout";
            }

            @Override
            public void perform(UiController uiController, View view) {
                AppBarLayout appBarLayout = (AppBarLayout) view;
                appBarLayout.setExpanded(false);
                uiController.loopMainThreadUntilIdle();
            }
        };
    }

    private static Matcher<View> moveToPage(final int page) {
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                return view instanceof ViewPager && ((ViewPager) view).getCurrentItem() == page;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("ViewPager should be on page " + page);
            }
        };
    }
}
