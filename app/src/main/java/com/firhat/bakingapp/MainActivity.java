package com.firhat.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

            getSupportFragmentManager().beginTransaction().add(R.id.container_recipe, recipeFragment)
                    .commit();
        }else{
            args.putBoolean(getString(R.string.is_tablet), true);
            recipeFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().add(R.id.container_recipe, recipeFragment)
                    .commit();
        }


    }
}
