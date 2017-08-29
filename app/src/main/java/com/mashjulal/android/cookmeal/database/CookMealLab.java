package com.mashjulal.android.cookmeal.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.mashjulal.android.cookmeal.objects.*;
import com.mashjulal.android.cookmeal.database.CookMealDBSchema.*;

import java.util.ArrayList;
import java.util.List;


public class CookMealLab {
    private CookMealDB mCookMealDB;

    private static CookMealLab sInstance;

    public static CookMealLab getInstance(Context context) {
        if (sInstance == null)
            sInstance = new CookMealLab(context);
        return sInstance;
    }

    private CookMealLab(Context context) {
        mCookMealDB = new CookMealDB(context);
    }

    public List<Recipe> getRecipes(){
        List<Recipe> recipes = new ArrayList<>();

        mCookMealDB.open();
        Cursor recipeCursor = mCookMealDB.getRecipesCursor();
        if (recipeCursor != null && recipeCursor.moveToFirst()) {
            do {
                Recipe recipe = getRecipeTitleInformation(recipeCursor);
                recipes.add(recipe);
            } while (recipeCursor.moveToNext());
            recipeCursor.close();
        }
        mCookMealDB.close();
        return recipes;
    }

    public Recipe getRecipeById(int recipeID){
        mCookMealDB.open();
        Cursor recipeCursor = mCookMealDB.getRecipeCursor(recipeID);
        if (recipeCursor == null || !recipeCursor.moveToFirst())
            return null;

        Recipe recipe = getRecipeInformation(recipeCursor);
        recipeCursor.close();
        mCookMealDB.close();
        return recipe;
    }

    public void addRecipe(Recipe recipe) {
        ContentValues cv = getContentValues(recipe);
        mCookMealDB.open();
        long recipeId = mCookMealDB.addData(cv, RecipesTable.TABLE_NAME);
        long ingredientId;
        for (Ingredient ingredient : recipe.getIngredientList()) {
            cv = getContentValues(ingredient);
            ingredientId = mCookMealDB.addData(cv, IngredientsTable.TABLE_NAME);
            cv = getContentValuesForIngredientListsTable(recipeId, ingredientId);
            mCookMealDB.addData(cv, IngredientListsTable.TABLE_NAME);
        }
        long stepId;
        for (Step step : recipe.getStepsList()) {
            cv = getContentValues(step);
            stepId = mCookMealDB.addData(cv, StepsTable.TABLE_NAME);
            cv = getContentValuesForStepListsTable(recipeId, stepId);
            mCookMealDB.addData(cv, StepListsTable.TABLE_NAME);
        }
        mCookMealDB.close();
    }

    private Recipe getRecipeTitleInformation(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(
                CookMealDBSchema.RecipesTable.Columns.ID));
        String name = cursor.getString(cursor.getColumnIndex(
                CookMealDBSchema.RecipesTable.Columns.NAME));
        String previewImagePath = cursor.getString(cursor.getColumnIndex(
                CookMealDBSchema.RecipesTable.Columns.PREVIEW_IMAGE_PATH));
        return new Recipe(id, name, previewImagePath);
    }

    private Recipe getRecipeInformation(Cursor cursor) {
        int id = cursor.getInt(
                cursor.getColumnIndex(CookMealDBSchema.RecipesTable.Columns.ID));
        Recipe recipe = getRecipeTitleInformation(cursor);
        recipe.setStepList(getStepList(id));
        recipe.setIngredientList(getIngredientList(id));
        return recipe;
    }

    private IngredientList getIngredientList(int recipeID) {
        IngredientList ingredientList = new IngredientList(recipeID);
        mCookMealDB.open();
        Cursor cursor = mCookMealDB.getIngredientsCursor(recipeID);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Ingredient ingredient = getIngredient(cursor);
                ingredientList.addIngredient(ingredient);
            } while (cursor.moveToNext());
            cursor.close();
        }
        mCookMealDB.close();
        return ingredientList;
    }

    private Ingredient getIngredient(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(
                CookMealDBSchema.IngredientsTable.Columns.ID));
        String name = cursor.getString(cursor.getColumnIndex(
                CookMealDBSchema.IngredientsTable.Columns.NAME));
        String type = cursor.getString(cursor.getColumnIndex(
                CookMealDBSchema.IngredientsTable.Columns.TYPE));
        int value = cursor.getInt(cursor.getColumnIndex(
                CookMealDBSchema.IngredientsTable.Columns.VALUE));

        return new Ingredient(id, name, type, value);
    }

    private StepList getStepList(int recipeID) {
        StepList stepList = new StepList(recipeID);
        mCookMealDB.open();
        Cursor cursor = mCookMealDB.getStepsCursor(recipeID);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Step step = getStep(cursor);
                stepList.add(step);
            } while (cursor.moveToNext());
            cursor.close();
        }
        mCookMealDB.close();
        return stepList;
    }

    private Step getStep(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(
                CookMealDBSchema.StepsTable.Columns.ID));
        int position = cursor.getInt(cursor.getColumnIndex(
                CookMealDBSchema.StepsTable.Columns.POSITION));
        String imagePath = cursor.getString(cursor.getColumnIndex(
                CookMealDBSchema.StepsTable.Columns.IMAGE_PATH));
        String description = cursor.getString(cursor.getColumnIndex(
                CookMealDBSchema.StepsTable.Columns.DESCRIPTION));
        long time = cursor.getLong(cursor.getColumnIndex(
                CookMealDBSchema.StepsTable.Columns.TIME));

        return new Step(id, position, imagePath, description, time);
    }

    private ContentValues getContentValues(Recipe recipe) {
        ContentValues cv = new ContentValues();
        if (recipe.getId() != -1)
            cv.put(RecipesTable.Columns.ID, recipe.getId());
        cv.put(RecipesTable.Columns.NAME, recipe.getName());
        cv.put(RecipesTable.Columns.PREVIEW_IMAGE_PATH, recipe.getPreviewImagePath());
        return cv;
    }

    private ContentValues getContentValues(Ingredient ingredient) {
        ContentValues cv = new ContentValues();
        if (ingredient.getId() != -1)
            cv.put(IngredientsTable.Columns.ID, ingredient.getId());
        cv.put(IngredientsTable.Columns.NAME, ingredient.getName());
        cv.put(IngredientsTable.Columns.TYPE, ingredient.getType());
        cv.put(IngredientsTable.Columns.VALUE, ingredient.getValue());
        return cv;
    }

    private ContentValues getContentValues(Step step) {
        ContentValues cv = new ContentValues();
        if (step.getId() != -1)
            cv.put(StepsTable.Columns.ID, step.getId());
        cv.put(StepsTable.Columns.POSITION, step.getStepNumber());
        cv.put(StepsTable.Columns.IMAGE_PATH, step.getImagePath());
        cv.put(StepsTable.Columns.DESCRIPTION, step.getDescription());
        cv.put(StepsTable.Columns.TIME, step.getTime());
        return cv;
    }

    private ContentValues getContentValuesForIngredientListsTable(long recipeId, long ingredientId) {
        ContentValues cv = new ContentValues();
        cv.put(IngredientListsTable.Columns.RECIPE_ID, recipeId);
        cv.put(IngredientListsTable.Columns.INGREDIENT_ID, ingredientId);
        return cv;
    }

    private ContentValues getContentValuesForStepListsTable(long recipeId, long stepId) {
        ContentValues cv = new ContentValues();
        cv.put(StepListsTable.Columns.RECIPE_ID, recipeId);
        cv.put(StepListsTable.Columns.STEP_ID, stepId);
        return cv;
    }

    public long getCookTimeForRecipe(int recipeId) {
        long cookTime = 0;
        mCookMealDB.open();
        Cursor cursor = mCookMealDB.getStepsCursor(recipeId);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                cookTime += cursor.getLong(cursor.getColumnIndex(StepsTable.Columns.TIME));
            } while (cursor.moveToNext());
            cursor.close();
        }
        mCookMealDB.close();
        return cookTime;
    }
}
