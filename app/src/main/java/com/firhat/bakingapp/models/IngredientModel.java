package com.firhat.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Macbook on 9/18/17.
 */

public class IngredientModel implements Parcelable{

    private float quantity;
    private String measure, ingredient;

    public IngredientModel(){

    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(quantity);
        parcel.writeString(measure);
        parcel.writeString(ingredient);

    }

    private IngredientModel(Parcel parcel){
        quantity    = parcel.readFloat();
        measure     = parcel.readString();
        ingredient  = parcel.readString();
    }

    public static final Parcelable.Creator<IngredientModel> CREATOR = new Parcelable.Creator<IngredientModel>(){

        @Override
        public IngredientModel createFromParcel(Parcel parcel) {
            return new IngredientModel(parcel);
        }

        @Override
        public IngredientModel[] newArray(int i) {
            return new IngredientModel[i];
        }
    };
}
