package com.fidflop.baketime;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.fidflop.baketime.data.Recipe;
import com.fidflop.baketime.ui.RecipeDetailActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.matcher.ViewMatchers.withSubstring;

@RunWith(AndroidJUnit4.class)
public class RecipseActivityDetailTest {

    private static final String RECIPE_NAME = "recipe name";

    @Rule
    public ActivityTestRule<RecipeDetailActivity> mActivityRule =
            new ActivityTestRule<RecipeDetailActivity>(RecipeDetailActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    Intent intent = new Intent(targetContext, RecipeDetailActivity.class);

                    Recipe recipe = new Recipe();
                    recipe.setName(RECIPE_NAME);
                    intent.putExtra("recipe", recipe);
                    return intent;
                }
            };

    @Test
    public void shouldShowRecipeName() {
        onView(withId(R.id.recipe_detail_ingredients)).check(matches(withSubstring(RECIPE_NAME)));
    }
}
