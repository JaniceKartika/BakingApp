package jkm.com.bakingapp.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.REAL;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

public interface IngredientColumns {

    @DataType(INTEGER)
    @PrimaryKey
    @AutoIncrement
    String ID = "_id";

    @DataType(REAL)
    @NotNull
    String QUANTITY = "quantity";

    @DataType(TEXT)
    @NotNull
    String MEASURE = "measure";

    @DataType(TEXT)
    @NotNull
    String INGREDIENT = "ingredient";

    @DataType(INTEGER)
    @References(table = RecipeDatabase.Tables.RECIPES, column = RecipeColumns.RECIPE_ID)
    String RECIPE_ID = "recipeId";
}
