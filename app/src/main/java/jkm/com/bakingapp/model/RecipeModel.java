package jkm.com.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RecipeModel implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("ingredients")
    private ArrayList<IngredientModel> ingredients;
    @SerializedName("steps")
    private ArrayList<StepModel> steps;
    @SerializedName("servings")
    private int servings;
    @SerializedName("image")
    private String image;

    public RecipeModel() {
        // Constructor
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<IngredientModel> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<IngredientModel> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<StepModel> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<StepModel> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeTypedList(this.ingredients);
        dest.writeTypedList(this.steps);
        dest.writeInt(this.servings);
        dest.writeString(this.image);
    }

    private RecipeModel(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.ingredients = in.createTypedArrayList(IngredientModel.CREATOR);
        this.steps = in.createTypedArrayList(StepModel.CREATOR);
        this.servings = in.readInt();
        this.image = in.readString();
    }

    public static final Parcelable.Creator<RecipeModel> CREATOR = new Parcelable.Creator<RecipeModel>() {
        @Override
        public RecipeModel createFromParcel(Parcel source) {
            return new RecipeModel(source);
        }

        @Override
        public RecipeModel[] newArray(int size) {
            return new RecipeModel[size];
        }
    };
}
