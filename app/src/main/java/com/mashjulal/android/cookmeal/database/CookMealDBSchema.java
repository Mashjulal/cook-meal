package com.mashjulal.android.cookmeal.database;

/**
 * Created by Master on 04.08.2017.
 */

public class CookMealDBSchema {

    public static final String DATABASE_NAME = "cook_meal.db";

    public static final class RecipesTable {
        public static final String TABLE_NAME = "recipes";

        public static final class Columns {
            public static final String
                    ID = "id",
                    NAME = "name",
                    PREVIEW_IMAGE_PATH = "preview_image_path";
        }

    }

    public static final class StepListsTable {
        public static final String TABLE_NAME = "step_lists";

        public static final class Columns {

            public static final String
                    RECIPE_ID = "recipe_id",
                    STEP_ID = "step_id";
        }

    }

    public static final class StepsTable {
        public static final String TABLE_NAME = "steps";

        public static final class Columns {

            public static final String
                    ID = "id",
                    POSITION = "position",
                    IMAGE_PATH = "image_path",
                    DESCRIPTION = "description",
                    TIME = "time";
        }

    }

    public static final class IngredientListsTable {
        public static final String TABLE_NAME = "ingredient_lists";

        public static final class Columns {

            public static final String
                    RECIPE_ID = "recipe_id",
                    INGREDIENT_ID = "ingredient_id";
        }

    }

    public static final class IngredientsTable {
        public static final String TABLE_NAME = "ingredients";

        public static final class Columns {
            public static final String
                    ID = "id",
                    NAME = "name",
                    TYPE = "type",
                    VALUE = "value";
        }

    }
}
