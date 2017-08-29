package com.mashjulal.android.cookmeal.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class IngredientList implements Serializable, Iterable<Ingredient> {

    private int mRecipeId;
    private List<Ingredient> mIngredients = new ArrayList<>();

    public IngredientList() {
        mIngredients = new ArrayList<>();
    }

    public IngredientList(int mRecipeId, List<Ingredient> mIngredients) {
        this.mRecipeId = mRecipeId;
        this.mIngredients = mIngredients;
    }

    public IngredientList(int recipeId) {
        mRecipeId = recipeId;
    }

    public IngredientList(List<Ingredient> mIngredients) {
        this.mIngredients = mIngredients;
    }

    public int getRecipeId() {
        return mRecipeId;
    }

    public List<Ingredient> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.mIngredients = ingredients;
    }

    public void removeIngredient(int position) {
        mIngredients.remove(position);
    }

    public void addIngredient(Ingredient ingredient) {
        mIngredients.add(ingredient);
    }

    public Ingredient getIngredient(int position) {
        return mIngredients.get(position);
    }

    public int size() {
        return mIngredients.size();
    }

    public boolean isEmpty() {
        return mIngredients.isEmpty();
    }

    @Override
    public Iterator<Ingredient> iterator() {
        return mIngredients.iterator();
    }
}
