package com.mashjulal.android.cookmeal.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mashjulal.android.cookmeal.R;
import com.mashjulal.android.cookmeal.activities.CookingActivity;
import com.mashjulal.android.cookmeal.activities.EditRecipeActivity;
import com.mashjulal.android.cookmeal.activities.RecipeScrollingActivity;
import com.mashjulal.android.cookmeal.adapters.RecipeCardRecyclerViewAdapter;
import com.mashjulal.android.cookmeal.database.CookMealLab;
import com.mashjulal.android.cookmeal.objects.Recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class RecipeBookFragment extends Fragment {

    public static final int REQUEST_CODE_RECIPE_ADDED = 1;
    public static final String PARAM_RECIPE_ID = "recipe_id";

    private Context mContext;
    private RecyclerView mRecyclerViewRecipes;
    private List<Recipe> mRecipes = new ArrayList<>();
    private RecipeCardRecyclerViewAdapter mViewAdapter;

    public RecipeBookFragment() {
        // Required empty public constructor
    }

    public static RecipeBookFragment newInstance() {
        return new RecipeBookFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = container.getContext();
        View v = inflater.inflate(R.layout.fragment_recipe_book, container, false);
        loadRecipesFromDB();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerViewRecipes = (RecyclerView) v.findViewById(R.id.rv_frRecipeBook_recipes);
        mRecyclerViewRecipes.setLayoutManager(layoutManager);
        mViewAdapter = new RecipeCardRecyclerViewAdapter(mContext, mRecipes);
        mViewAdapter.setOnViewClickListener(new RecipeCardRecyclerViewAdapter.OnViewClickListener() {
            @Override
            public void onViewClick(int position) {
                openRecipeViewActivity(position);
            }

            @Override
            public void onStartButtonClick(int position) {
                openCookingActivity(position);
            }
        });
        mRecyclerViewRecipes.setAdapter(mViewAdapter);
        setHasOptionsMenu(true);

        FloatingActionButton fabCreateRecipe =
                (FloatingActionButton) v.findViewById(R.id.floatingActionButton);
        fabCreateRecipe.setOnClickListener(v1 -> {
            Intent intent = new Intent(mContext, EditRecipeActivity.class);
            startActivityForResult(intent, REQUEST_CODE_RECIPE_ADDED);
        });
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_fragment_recipe_book, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_mRecipeBook_search:
                Toast.makeText(mContext, "SEARCHING", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.gi_mRecipeBook_name:
                item.setChecked(true);
                Collections.sort(mRecipes, Recipe.getNameComparator());
                mViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.gi_mRecipeBook_time:
                item.setChecked(true);
                Collections.sort(mRecipes, Recipe.getTimeComparator());
                mViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.gi_mRecipeBook_default:
                item.setChecked(true);
                Collections.sort(mRecipes, Recipe.getDefaultComparator());
                mViewAdapter.notifyDataSetChanged();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadRecipesFromDB() {
        CookMealLab lab = CookMealLab.getInstance(mContext);
        mRecipes = lab.getRecipes();
    }

    private void openRecipeViewActivity(int position) {
        Intent intent = new Intent(mContext, RecipeScrollingActivity.class);
        intent.putExtra(PARAM_RECIPE_ID, mRecipes.get(position).getId());
        startActivity(intent);
    }

    private void openCookingActivity(int position) {
        Intent intent = new Intent(mContext, CookingActivity.class);
        intent.putExtra(PARAM_RECIPE_ID, mRecipes.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_RECIPE_ADDED) {
                loadRecipesFromDB();
                mViewAdapter.notifyDataSetChanged();
            }
        }
    }
}
