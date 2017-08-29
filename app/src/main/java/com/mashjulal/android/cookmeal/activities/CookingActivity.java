package com.mashjulal.android.cookmeal.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.mashjulal.android.cookmeal.R;
import com.mashjulal.android.cookmeal.database.CookMealLab;
import com.mashjulal.android.cookmeal.fragments.StepFragment;
import com.mashjulal.android.cookmeal.objects.Recipe;
import com.mashjulal.android.cookmeal.objects.Step;

import static com.mashjulal.android.cookmeal.fragments.RecipeBookFragment.PARAM_RECIPE_ID;

public class CookingActivity extends AppCompatActivity {

    // Activity debugging tag
    private static final String TAG = CookingActivity.class.getSimpleName();

    private static final int CODE_PREV_BUTTON_FIRST = 0;
    private static final int CODE_BUTTON_MIDDLE = 1;
    private static final int CODE_NEXT_BUTTON_LAST = 2;

    private Recipe mRecipe;
    private ViewPager mViewPagerSteps;
    private Button mButtonPrev;
    private Button mButtonNext;

    private View.OnClickListener onPrevClick = v -> onPrevClick();
    private View.OnClickListener onNextClick = v -> onNextClick();
    private View.OnClickListener onFinishClick = v -> onFinishClick();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking);

        Bundle args = getIntent().getExtras();
        if (args.containsKey(PARAM_RECIPE_ID)) {
            int recipeId = args.getInt(PARAM_RECIPE_ID);
            mRecipe = CookMealLab.getInstance(this).getRecipeById(recipeId);
        } else {
            finish();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mRecipe.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewPagerSteps = (ViewPager) findViewById(R.id.dp_aCooking_steps);
        mViewPagerSteps.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Step step = mRecipe.getStepsList().get(position);
                StepFragment fragment = StepFragment.newInstance(step);
                if (step.getTime() != 0) {
                    fragment.setOnFinishTimerListener(() -> mButtonNext.setVisibility(View.VISIBLE));
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return mRecipe.getStepsList().size();
            }
        });
        // Disable fragment change on swipe action
        mViewPagerSteps.setOnTouchListener((v, event) -> true);

        mButtonPrev = (Button) findViewById(R.id.btn_aCooking_prev);
        mButtonNext = (Button) findViewById(R.id.btn_aCooking_next);
        mButtonPrev.setOnClickListener(onPrevClick);
        setupButtons();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(mRecipe.getName())
                .setMessage("Stop cooking this meal?")
                .setPositiveButton("Yes", (dialog, which) -> CookingActivity.super.onBackPressed())
                .setNegativeButton("No", null)
                .create()
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onPrevClick() {
        int currentItemIndex = mViewPagerSteps.getCurrentItem();
        if (currentItemIndex != 0) {
            mViewPagerSteps.setCurrentItem(currentItemIndex - 1);
            changePrevButtonState((currentItemIndex == 1) ?
                    CODE_PREV_BUTTON_FIRST : CODE_BUTTON_MIDDLE);
        }
        changeNextButtonState(CODE_BUTTON_MIDDLE);
    }

    private void onNextClick() {
        int currentItemIndex = mViewPagerSteps.getCurrentItem();
        if (currentItemIndex != mRecipe.getStepsList().size() - 1) {
            mViewPagerSteps.setCurrentItem(currentItemIndex + 1);
            changeNextButtonState((currentItemIndex == mRecipe.getStepsList().size() - 2) ?
                    CODE_NEXT_BUTTON_LAST : CODE_BUTTON_MIDDLE);
        }
        changePrevButtonState(CODE_BUTTON_MIDDLE);
    }

    private void changePrevButtonState(int state) {
        switch (state) {
            case CODE_PREV_BUTTON_FIRST:
                mButtonPrev.setVisibility(View.INVISIBLE);
                break;
            case CODE_BUTTON_MIDDLE:
                mButtonPrev.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void changeNextButtonState(int state) {
        switch (state) {
            case CODE_NEXT_BUTTON_LAST:
                mButtonNext.setText("Finish");
                mButtonNext.setOnClickListener(onFinishClick);
                break;
            case CODE_BUTTON_MIDDLE:
                mButtonNext.setText("Next");
                mButtonNext.setOnClickListener(onNextClick);
                break;
        }
        mButtonNext.setVisibility(
                (mRecipe.getStepsList().get(mViewPagerSteps.getCurrentItem()).getTime() == 0) ?
                        View.VISIBLE :
                        View.INVISIBLE);
    }

    private void setupButtons() {
        int stepListSize = mRecipe.getStepsList().size();
        switch (stepListSize) {
            case 1:
                changePrevButtonState(CODE_PREV_BUTTON_FIRST);
                changeNextButtonState(CODE_NEXT_BUTTON_LAST);
                break;
            default:
                changePrevButtonState(CODE_PREV_BUTTON_FIRST);
                changeNextButtonState(CODE_BUTTON_MIDDLE);
                break;
        }
    }

    private void onFinishClick() {
        finish();
    }
}
