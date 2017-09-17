package jkm.com.bakingapp.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.Constraints;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.UniqueConstraint;

import static net.simonvt.schematic.annotation.ConflictResolutionType.REPLACE;
import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

@Constraints(unique = @UniqueConstraint(
        columns = {RecipeColumns.RECIPE_ID},
        onConflict = REPLACE)
)

public interface RecipeColumns {

    @DataType(INTEGER)
    @PrimaryKey
    @AutoIncrement
    String ID = "_id";

    @DataType(INTEGER)
    @NotNull
    String RECIPE_ID = "recipeId";

    @DataType(TEXT)
    @NotNull
    String NAME = "name";

    @DataType(INTEGER)
    @NotNull
    String SERVINGS = "servings";

    @DataType(TEXT)
    @NotNull
    String IMAGE = "image";

    String INGREDIENTS = "ingredients";
}
