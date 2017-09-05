package jkm.com.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import jkm.com.bakingapp.model.RecipeModel;
import jkm.com.bakingapp.util.RecipeAssets;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailFragment.OnStepClickListener,
        StepDetailFragment.OnPageSelected {

    @BindView(R.id.iv_recipe_detail)
    ImageView recipeDetailImageView;
    @BindView(R.id.toolbar_recipe_detail)
    Toolbar toolbar;

    private RecipeModel recipeModel;
    private boolean isTwoPane;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent.hasExtra(getString(R.string.recipes_key))) {
            recipeModel = intent.getParcelableExtra(getString(R.string.recipes_key));
        } else {
            Toast.makeText(this, getString(R.string.failed_show_recipe_detail), Toast.LENGTH_LONG).show();
            return;
        }

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.linear_layout_two_pane) != null) {
            isTwoPane = true;
            View toolbarShadow = findViewById(R.id.toolbar_shadow_recipe_detail);
            toolbarShadow.bringToFront();

            if (savedInstanceState == null) {
                StepDetailFragment stepDetailFragment = new StepDetailFragment();
                stepDetailFragment.setStepModels(recipeModel.getSteps());
                stepDetailFragment.setInitialPosition(0);

                setTitle(recipeModel.getSteps().get(0).getShortDescription());

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.step_detail_container, stepDetailFragment)
                        .commit();
            }
        } else {
            isTwoPane = false;
            int orientation = getResources().getConfiguration().orientation;
            setToolbarViewParams(orientation);

            CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout)
                    findViewById(R.id.collapsing_toolbar_recipe_detail);
            collapsingToolbar.setTitle(recipeModel.getName());
            collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
            collapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
        }

        String imagePath = recipeModel.getImage() == null ? "" : recipeModel.getImage();
        if (imagePath.isEmpty()) {
            recipeDetailImageView.setImageResource(RecipeAssets.getRecipeImageID(recipeModel.getName()));
        } else {
            Picasso.with(this)
                    .load(imagePath)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(recipeDetailImageView);
        }

        if (savedInstanceState == null) {
            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setRecipeModel(recipeModel);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_detail_container, recipeDetailFragment)
                    .commit();
        }
    }

    @Override
    public void setOnPageSelected(int position) {
        setTitle(recipeModel.getSteps().get(position).getShortDescription());
    }

    @Override
    public void onStepSelected(int position) {
        if (isTwoPane) {
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setStepModels(recipeModel.getSteps());
            stepDetailFragment.setInitialPosition(position);

            setTitle(recipeModel.getSteps().get(position).getShortDescription());

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.step_detail_container, stepDetailFragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putParcelableArrayListExtra(getString(R.string.steps_key), recipeModel.getSteps());
            intent.putExtra(getString(R.string.step_position_key), position);
            startActivity(intent);
        }
    }

    private void setToolbarViewParams(int orientation) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;

        int imageHeight;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            imageHeight = (width * 2) / 3;
        } else {
            imageHeight = (height * 4 / 9);
        }

        CollapsingToolbarLayout.LayoutParams layoutParamsForImageView = new CollapsingToolbarLayout
                .LayoutParams(CollapsingToolbarLayout.LayoutParams.MATCH_PARENT, imageHeight);
        recipeDetailImageView.setLayoutParams(layoutParamsForImageView);
    }

    private void setTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
