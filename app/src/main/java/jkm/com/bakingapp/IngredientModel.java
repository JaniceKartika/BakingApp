package jkm.com.bakingapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class IngredientModel implements Parcelable {
    @SerializedName("quantity")
    private float quantity;
    @SerializedName("measure")
    private String measure;
    @SerializedName("ingredient")
    private String ingredient;

    public IngredientModel() {
        // Constructor
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.quantity);
        dest.writeString(this.measure);
        dest.writeString(this.ingredient);
    }

    private IngredientModel(Parcel in) {
        this.quantity = in.readFloat();
        this.measure = in.readString();
        this.ingredient = in.readString();
    }

    public static final Parcelable.Creator<IngredientModel> CREATOR = new Parcelable.Creator<IngredientModel>() {
        @Override
        public IngredientModel createFromParcel(Parcel source) {
            return new IngredientModel(source);
        }

        @Override
        public IngredientModel[] newArray(int size) {
            return new IngredientModel[size];
        }
    };
}
