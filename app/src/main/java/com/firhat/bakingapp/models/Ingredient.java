package com.firhat.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Macbook on 9/18/17.
 */

public class Ingredient implements Parcelable{

    private float quantity;
    private String measure, ingredient;

    public Ingredient(){

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

    private Ingredient(Parcel parcel){
        quantity    = parcel.readFloat();
        measure     = parcel.readString();
        ingredient  = parcel.readString();
    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>(){

        @Override
        public Ingredient createFromParcel(Parcel parcel) {
            return new Ingredient(parcel);
        }

        @Override
        public Ingredient[] newArray(int i) {
            return new Ingredient[i];
        }
    };
}
