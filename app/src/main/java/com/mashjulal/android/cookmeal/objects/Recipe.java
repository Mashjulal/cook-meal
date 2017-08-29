package com.mashjulal.android.cookmeal.objects;

import java.io.Serializable;
import java.util.Comparator;


public class Recipe implements Serializable {

    private int mId = -1;
    private String mName = "";
    private String mPreviewImagePath = "";
    private StepList mStepList = new StepList();
    private IngredientList mIngredientList = new IngredientList();

    public Recipe() {
    }

    public Recipe(int id, String name, String previewImagePath) {
        mId = id;
        mName = name;
        mPreviewImagePath = previewImagePath;
    }

    public Recipe(int id, String name, String previewImagePath, StepList stepList, IngredientList ingredientList) {
        mId = id;
        mName = name;
        mPreviewImagePath = previewImagePath;
        mStepList = stepList;
        mIngredientList = ingredientList;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPreviewImagePath() {
        return mPreviewImagePath;
    }

    public void setPreviewImagePath(String previewImagePath) {
        mPreviewImagePath = previewImagePath;
    }

    public StepList getStepsList() {
        return mStepList;
    }

    public void setStepList(StepList stepList) {
        mStepList = stepList;
    }

    public IngredientList getIngredientList() {
        return mIngredientList;
    }

    public void setIngredientList(IngredientList ingredientList) {
        mIngredientList = ingredientList;
    }

    public static Comparator<Recipe> getNameComparator() {
        return (o1, o2) -> o1.getName().compareTo(o2.getName());
    }

    public static Comparator<Recipe> getDefaultComparator() {
        return (o1, o2) -> Integer.valueOf(o1.getId()).compareTo(o2.getId());
    }

    public static Comparator<Recipe> getTimeComparator() {
        return (o1, o2) -> Long.valueOf(o1.getStepsList().getTotalTime())
                .compareTo(o2.getStepsList().getTotalTime());
    }
}
