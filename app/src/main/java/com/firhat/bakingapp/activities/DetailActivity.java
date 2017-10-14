package com.firhat.bakingapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firhat.bakingapp.R;
import com.firhat.bakingapp.adapters.StepAdapter;
import com.firhat.bakingapp.fragments.DetailStepFragment;
import com.firhat.bakingapp.fragments.StepFragment;
import com.firhat.bakingapp.models.Recipe;
import com.firhat.bakingapp.models.Step;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements StepAdapter.ListItemClickListener {

    Recipe recipe;
    List<Step> stepList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        recipe = intent.getParcelableExtra("PARCEL_RECIPE");
        stepList = recipe.getSteps();

        if(savedInstanceState == null){
            StepFragment stepFragment = new StepFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("PARCEL_STEP", recipe);
            stepFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_step, stepFragment)
                    .commit();

            if(findViewById(R.id.linear_layout_tablet) != null){
                Step step = stepList.get(0);
                DetailStepFragment detailStepFragment = new DetailStepFragment();

                Bundle stepArgs = new Bundle();
                stepArgs.putParcelable(getResources().getString(R.string.parcel_step), step);
                detailStepFragment.setArguments(stepArgs);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_step_detail, detailStepFragment)
                        .commit();
            }

        }

    }

    public void nextStep(){

    }

    @Override
    public void onListItemClick(List<Step> stepsOut, int clickedItemIndex) {
        Step listStep = stepList.get(clickedItemIndex);

        DetailStepFragment fragment = new DetailStepFragment();
        Bundle args = new Bundle();
        args.putParcelable(getResources().getString(R.string.parcel_step), listStep);
        args.putParcelable(getResources().getString(R.string.parcel_step_data), recipe);
        args.putString("index", String.valueOf(clickedItemIndex));
        fragment.setArguments(args);

        if (findViewById(R.id.linear_layout) != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_step, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            // tablet layout
            //  Log.e(TAG, "list of step : " + listStep.getDescription());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_step_detail, fragment)
                    .addToBackStack(null)
                    .commit();
        }

    }
}
