package com.mashjulal.android.cookmeal.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class StepList implements Serializable, Iterable<Step> {

    private int mRecipeId;
    private List<Step> mSteps = new ArrayList<>();

    public StepList() {
    }

    public StepList(int mRecipeId, List<Step> mSteps) {
        this.mRecipeId = mRecipeId;
        this.mSteps = mSteps;
    }

    public StepList(int recipeId) {
        mRecipeId = recipeId;
    }

    public StepList(List<Step> mSteps) {
        this.mSteps = mSteps;
    }

    public int getRecipeId() {
        return mRecipeId;
    }

    public List<Step> getSteps() {
        return mSteps;
    }

    public void setSteps(List<Step> Steps) {
        this.mSteps = Steps;
    }

    public void remove(int position) {
        mSteps.remove(position);
    }

    public void add(Step Step) {
        mSteps.add(Step);
    }

    public Step get(int position) {
        return mSteps.get(position);
    }

    public int size() {
        return mSteps.size();
    }

    public boolean isEmpty() {
        return mSteps.isEmpty();
    }

    public long getTotalTime() {
        long totalTime = 0;
        for (Step step : mSteps)
            totalTime += step.getTime();
        return totalTime;
    }

    @Override
    public Iterator<Step> iterator() {
        return mSteps.iterator();
    }
}
