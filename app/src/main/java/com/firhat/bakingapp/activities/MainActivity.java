package com.firhat.bakingapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firhat.bakingapp.R;
import com.firhat.bakingapp.fragments.RecipeFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecipeFragment recipeFragment = new RecipeFragment();
        Bundle args = new Bundle();

        if (findViewById(R.id.container_recipe) != null){
            args.putBoolean(getString(R.string.is_tablet), false);
            recipeFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().replace(R.id.container_recipe, recipeFragment)
                    .commit();
        }else{
            args.putBoolean(getString(R.string.is_tablet), true);
            recipeFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().replace(R.id.container_recipe_land, recipeFragment)
                    .commit();
        }


    }
}
