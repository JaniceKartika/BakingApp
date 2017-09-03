package jkm.com.bakingapp;

public class RecipeAssets {
    public static int getRecipeImageID(String recipeName) {
        switch (recipeName) {
            case "Nutella Pie":
                return R.drawable.nutella_pie;
            case "Brownies":
                return R.drawable.brownies;
            case "Yellow Cake":
                return R.drawable.yellow_cake;
            case "Cheesecake":
                return R.drawable.cheese_cake;
            default:
                return R.drawable.placeholder;
        }
    }
}
