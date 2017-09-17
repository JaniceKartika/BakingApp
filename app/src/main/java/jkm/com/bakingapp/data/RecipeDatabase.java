package jkm.com.bakingapp.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.IfNotExists;
import net.simonvt.schematic.annotation.Table;

@Database(version = RecipeDatabase.VERSION)
public final class RecipeDatabase {

    public static final int VERSION = 1;

    public static class Tables {
        @Table(RecipeColumns.class)
        @IfNotExists
        public static final String RECIPES = "recipes";
    }

    @Table(IngredientColumns.class)
    @IfNotExists
    public static final String INGREDIENTS = "ingredients";
}
