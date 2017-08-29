package com.mashjulal.android.cookmeal.activities;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.mashjulal.android.cookmeal.R;
import com.mashjulal.android.cookmeal.adapters.IngredientItemRecyclerViewAdapter;
import com.mashjulal.android.cookmeal.adapters.RecipeStepCardRecyclerViewAdapter;
import com.mashjulal.android.cookmeal.database.CookMealLab;
import com.mashjulal.android.cookmeal.objects.Recipe;

import static com.mashjulal.android.cookmeal.fragments.RecipeBookFragment.PARAM_RECIPE_ID;

public class RecipeScrollingActivity extends AppCompatActivity {

    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_scrolling);

        Bundle args = getIntent().getExtras();
        if (args.containsKey(PARAM_RECIPE_ID)) {
            int recipeId = args.getInt(PARAM_RECIPE_ID);
            mRecipe = CookMealLab.getInstance(this).getRecipeById(recipeId);
        } else {
            finish();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (!mRecipe.getPreviewImagePath().isEmpty()) {
            ImageView ivToolbar = (ImageView) findViewById(R.id.iv_aRecipe_previewImage);
            ivToolbar.setImageBitmap(BitmapFactory.decodeFile(mRecipe.getPreviewImagePath()));
            ivToolbar.setVisibility(View.VISIBLE);
        }
        CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        ctl.setTitle(mRecipe.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerViewSteps = (RecyclerView) findViewById(R.id.rv_aRecipe_stepCards);
        recyclerViewSteps.setLayoutManager(new LinearLayoutManager(this));
        RecipeStepCardRecyclerViewAdapter adapter =
                new RecipeStepCardRecyclerViewAdapter(this, mRecipe.getStepsList(), false);
        recyclerViewSteps.setAdapter(adapter);

        RecyclerView recyclerViewIngredients = (RecyclerView) findViewById(R.id.rv_aRecipe_ingredients);
        recyclerViewIngredients.setLayoutManager(new LinearLayoutManager(this));
        IngredientItemRecyclerViewAdapter adapterIngredients =
                new IngredientItemRecyclerViewAdapter(this, mRecipe.getIngredientList(), false);
        recyclerViewIngredients.setAdapter(adapterIngredients);
    }
}
