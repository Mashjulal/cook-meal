package com.mashjulal.android.cookmeal.objects;

import java.io.Serializable;

/**
 * Created by Master on 18.08.2017.
 */

public class Step implements Serializable {

    private int mId = -1;
    private int mStepNumber = 0;
    private String mImagePath;
    private String mDescription;
    private long mTime;

    public Step(int id, int stepNumber, String imagePath, String description, long time) {
        mId = id;
        mStepNumber = stepNumber;
        mImagePath = imagePath;
        mDescription = description;
        mTime = time;
    }

    public Step() {
        mImagePath = "";
        mDescription = "";
        mTime = 0;
    }

    public int getId() {
        return mId;
    }


    public int getStepNumber() {
        return mStepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.mStepNumber = stepNumber;
    }

    public String getImagePath() {
        return mImagePath;
    }

    public void setImagePath(String imagePath) {
        this.mImagePath = imagePath;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }
}
