package com.firhat.bakingapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firhat.bakingapp.R;
import com.firhat.bakingapp.activities.DetailActivity;
import com.firhat.bakingapp.adapters.RecipeAdapter;
import com.firhat.bakingapp.models.Recipe;
import com.firhat.bakingapp.utils.NetworkUtils;
import com.firhat.bakingapp.utils.RecyclerClickListener;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;



public class RecipeFragment extends Fragment {

    private final static String TAG = RecipeFragment.class.getSimpleName();

    ProgressBar loading;
    RecyclerView recyclerView;

    boolean isTablet;

    GridLayoutManager gridLayoutManager;

    Recipe[] arrRecipe;


    public RecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        loading = (ProgressBar)  view.findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView)  view.findViewById(R.id.recycler_view);

        if(getArguments() != null){
            isTablet = getArguments().getBoolean("IS_TABLET");
        }

        if(!isTablet){
            gridLayoutManager = new GridLayoutManager(getActivity(), 2);
            recyclerView.setLayoutManager(gridLayoutManager);
        }else{
            gridLayoutManager = new GridLayoutManager(getActivity(), 4);
            recyclerView.setLayoutManager(gridLayoutManager);
        }

        URL url = NetworkUtils.buildUrl();

        getRecipe(url);

        recyclerView.addOnItemTouchListener(new RecyclerClickListener(getContext(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Recipe recipe = arrRecipe[position];
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(getResources().getString(R.string.parcel_recipe), recipe);
                startActivity(intent);
            }
        }));
    }

    private void getRecipe(URL url) {
        if (isNetworkAvailable()) {
            new FetchData().execute(url);
        } else {
            Toast.makeText(getContext(), getResources().getString(R.string.error_need_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    private class FetchData extends AsyncTask<URL, Void, Recipe[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected Recipe[] doInBackground(URL... params) {
            URL url = params[0];
            try {
                String json = NetworkUtils.getResponseFromHttp(url);
                return NetworkUtils.getRecipeDataFromJson(json);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Recipe[] recipes) {
            super.onPostExecute(recipes);
            if (recipes == null) {
                Toast.makeText(getContext(), "error occurred", Toast.LENGTH_SHORT).show();
                return;
            }
            loading.setVisibility(View.INVISIBLE);
            arrRecipe = recipes;

            recyclerView.setAdapter(new RecipeAdapter(arrRecipe, getContext()));
        }
    }




}
