package com.firhat.bakingapp.fragments;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firhat.bakingapp.R;
import com.firhat.bakingapp.activities.DetailActivity;
import com.firhat.bakingapp.adapters.IngredientAdapter;
import com.firhat.bakingapp.adapters.StepAdapter;
import com.firhat.bakingapp.models.IngredientModel;
import com.firhat.bakingapp.models.Recipe;
import com.firhat.bakingapp.models.Step;
import com.firhat.bakingapp.widget.UpdateBakingService;

import java.util.ArrayList;
import java.util.List;


public class StepFragment extends Fragment {

    Recipe mRecipe;

    RecyclerView ingredientRecyclerView;
    RecyclerView stepRecyclerView;

    List<IngredientModel> ingredient = null;
    List<Step> step = null;

    LinearLayoutManager layoutManager;
    LinearLayoutManager linearLayoutManager;

    StepAdapter stepAdapter;
    IngredientAdapter ingredientAdapter;

    Parcelable ingredientListState;
    Parcelable stepListState;

    public StepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.display_steps, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(getResources().getString(R.string.parcel_recipe));

            ingredientListState = savedInstanceState.getParcelable(getResources().getString(R.string.saved_layout_manager_ingredient));
            stepListState = savedInstanceState.getParcelable(getResources().getString(R.string.saved_layout_manager_step));
        } else {
            if (getArguments() != null) {
                mRecipe = getArguments().getParcelable(getResources().getString(R.string.parcel_step));
            }
        }

        if (mRecipe != null) {
            ingredient = mRecipe.getIngredients();
            step = mRecipe.getSteps();
        }

        ingredientRecyclerView = (RecyclerView) view.findViewById(R.id.ingredient_recycler_view);
        stepRecyclerView = (RecyclerView) view.findViewById(R.id.step_recycler_view);

        stepAdapter = new StepAdapter(step, (DetailActivity) getActivity());
        ingredientAdapter = new IngredientAdapter(ingredient);

        layoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager= new LinearLayoutManager(getActivity());

        ingredientRecyclerView.setLayoutManager(layoutManager);
        stepRecyclerView.setLayoutManager(linearLayoutManager);

        ingredientRecyclerView.setAdapter(ingredientAdapter);
        stepRecyclerView.setAdapter(stepAdapter);

        final ArrayList<String> recipeIngredientForWidgets = new ArrayList<>();

        for (int i = 0; i < ingredient.size(); i++) {

            String ingredientName = ingredient.get(i).getIngredient();
            float quantity = ingredient.get(i).getQuantity();
            String measure = ingredient.get(i).getMeasure();

            recipeIngredientForWidgets.add(ingredientName + "\n" + "Quantity: " + quantity + "\n" + "Measure: " + measure + "\n");
        }

        UpdateBakingService.startBakingService(getContext(), recipeIngredientForWidgets);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getResources().getString(R.string.parcel_recipe), mRecipe);

        outState.putParcelable(getResources().getString(R.string.saved_layout_manager_ingredient), ingredientRecyclerView.getLayoutManager().onSaveInstanceState());
        outState.putParcelable(getResources().getString(R.string.saved_layout_manager_step), stepRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onResume() {
        super.onResume();

        if (ingredientListState != null) {
            ingredientRecyclerView.getLayoutManager().onRestoreInstanceState(ingredientListState);
        }

        if (stepListState != null) {
            stepRecyclerView.getLayoutManager().onRestoreInstanceState(stepListState);
        }
    }
}
