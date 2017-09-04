package jkm.com.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import jkm.com.bakingapp.adapter.IngredientAdapter;
import jkm.com.bakingapp.adapter.StepAdapter;
import jkm.com.bakingapp.model.RecipeModel;

public class RecipeDetailFragment extends Fragment implements StepAdapter.OnItemClickListener {

    @BindView(R.id.tv_recipe_name_detail)
    TextView recipeNameTextView;
    @BindView(R.id.rv_recipe_ingredients_detail)
    RecyclerView ingredientsRecyclerView;
    @BindView(R.id.rv_recipe_steps_detail)
    RecyclerView stepsRecyclerView;

    private RecipeModel recipeModel;

    private OnStepClickListener mCallback;

    public RecipeDetailFragment() {
        // Constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnStepClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, view);

        if (savedInstanceState != null) {
            recipeModel = savedInstanceState.getParcelable(getString(R.string.recipes_key));
        }

        recipeNameTextView.setText(recipeModel == null ? "" : recipeModel.getName());

        configureRecyclerView(ingredientsRecyclerView);
        IngredientAdapter ingredientAdapter = new IngredientAdapter(getContext(), recipeModel.getIngredients());
        ingredientsRecyclerView.setAdapter(ingredientAdapter);

        configureRecyclerView(stepsRecyclerView);
        stepsRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        StepAdapter stepAdapter = new StepAdapter(getContext(), recipeModel.getSteps(), this);
        stepsRecyclerView.setAdapter(stepAdapter);

        return view;
    }

    @Override
    public void setOnItemClickListener(View view, int position) {
        if (mCallback != null) {
            mCallback.onStepSelected(position);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.recipes_key), recipeModel);
    }

    public void setRecipeModel(RecipeModel recipeModel) {
        this.recipeModel = recipeModel;
    }

    private void configureRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
    }

    interface OnStepClickListener {
        void onStepSelected(int position);
    }
}
