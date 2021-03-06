package com.firhat.bakingapp.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firhat.bakingapp.R;
import com.firhat.bakingapp.models.IngredientModel;

import java.util.List;



public class IngredientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<IngredientModel> mIngredients;

    public void setData(List<IngredientModel> ingredients) {
        mIngredients = ingredients;
        notifyDataSetChanged();
    }

    public IngredientAdapter(List<IngredientModel> ingredients) {
        mIngredients = ingredients;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder;

        View view = layoutInflater.inflate(R.layout.ingredient_list_item, parent, false);
        viewHolder = new IngredientViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        configureViewHolder((IngredientViewHolder) holder, position);
    }

    @Override
    public int getItemCount() {
        if (mIngredients != null) {
            return mIngredients.size();
        }
        return 0;
    }

    private void configureViewHolder(IngredientViewHolder ingredientViewHolder, int position) {
        IngredientModel ingredient = mIngredients.get(position);

        ingredientViewHolder.ingredientTextView.setText(ingredient.getIngredient());

        String quantity = String.valueOf(ingredient.getQuantity());
        ingredientViewHolder.quantityTextView.setText(quantity);

        ingredientViewHolder.measureTextView.setText(ingredient.getMeasure());
    }

    private class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView ingredientTextView;
        TextView quantityTextView;
        TextView measureTextView;

        public IngredientViewHolder(View itemView) {
            super(itemView);

            ingredientTextView = (TextView) itemView.findViewById(R.id.tv_ingredient);
            quantityTextView = (TextView) itemView.findViewById(R.id.tv_ingredient_quantity);
            measureTextView = (TextView) itemView.findViewById(R.id.tv_ingredient_unit);
        }
    }
}
