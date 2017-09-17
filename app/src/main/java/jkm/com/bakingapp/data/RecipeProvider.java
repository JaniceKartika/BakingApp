package jkm.com.bakingapp.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.MapColumns;
import net.simonvt.schematic.annotation.TableEndpoint;

import java.util.HashMap;
import java.util.Map;

@ContentProvider(authority = RecipeProvider.AUTHORITY, database = RecipeDatabase.class)
public final class RecipeProvider {

    static final String AUTHORITY = "jkm.com.bakingapp";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String RECIPES = "recipes";
        String INGREDIENTS = "ingredients";
        String FROM_RECIPE = "fromRecipe";
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = RecipeDatabase.Tables.RECIPES)
    public static class Recipes {
        @MapColumns
        public static Map<String, String> mapColumns() {
            Map<String, String> map = new HashMap<>();
            map.put(RecipeColumns.INGREDIENTS, INGREDIENT_COUNT);
            return map;
        }

        @ContentUri(
                path = Path.RECIPES,
                type = "vnd.android.cursor.dir/recipe",
                defaultSort = RecipeColumns.RECIPE_ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.RECIPES);

        @InexactContentUri(
                path = Path.RECIPES + "/#",
                name = "RECIPE_ID",
                type = "vnd.android.cursor.item/recipe",
                whereColumn = RecipeColumns.RECIPE_ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.RECIPES, String.valueOf(id));
        }

        static final String INGREDIENT_COUNT = "(SELECT COUNT(*) FROM "
                + RecipeDatabase.INGREDIENTS
                + " WHERE "
                + RecipeDatabase.INGREDIENTS
                + "."
                + IngredientColumns.RECIPE_ID
                + "="
                + RecipeDatabase.Tables.RECIPES
                + "."
                + RecipeColumns.RECIPE_ID
                + ")";
    }

    @TableEndpoint(table = RecipeDatabase.INGREDIENTS)
    public static class Ingredients {

        @ContentUri(
                path = Path.INGREDIENTS,
                type = "vnd.android.cursor.dir/ingredient",
                defaultSort = IngredientColumns.ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.INGREDIENTS);

        @InexactContentUri(
                name = "INGREDIENTS_FROM_RECIPE",
                path = Path.INGREDIENTS + "/" + Path.FROM_RECIPE + "/#",
                type = "vnd.android.cursor.dir/recipe",
                whereColumn = IngredientColumns.RECIPE_ID,
                pathSegment = 2,
                defaultSort = IngredientColumns.ID + " ASC")
        public static Uri fromRecipe(long recipeId) {
            return buildUri(Path.INGREDIENTS, Path.FROM_RECIPE, String.valueOf(recipeId));
        }
    }
}
