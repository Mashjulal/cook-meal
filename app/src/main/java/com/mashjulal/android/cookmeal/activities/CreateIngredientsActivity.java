package com.mashjulal.android.cookmeal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mashjulal.android.cookmeal.R;
import com.mashjulal.android.cookmeal.adapters.IngredientItemRecyclerViewAdapter;
import com.mashjulal.android.cookmeal.objects.Ingredient;
import com.mashjulal.android.cookmeal.objects.IngredientList;

public class CreateIngredientsActivity extends AppCompatActivity {

    private static final String TAG = CreateIngredientsActivity.class.getSimpleName();

    public static final String INGREDIENTS = "ingredients";

    private IngredientItemRecyclerViewAdapter mViewAdapter;
    private RecyclerView mRecyclerView;
    private IngredientList mIngredientList;

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    int id = viewHolder.getAdapterPosition();
                    mIngredientList.removeIngredient(id);
                    mViewAdapter.notifyItemRemoved(id);
                }
            };

    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingedients);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_aCreateTimeProgram);
        toolbar.setTitle(getString(R.string.title_create_recipe));
        setSupportActionBar(toolbar);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_aAddIngredients_entries);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, llm.getOrientation()));

        Bundle args = getIntent().getExtras();
        if (args != null && args.containsKey(INGREDIENTS)) {
            mIngredientList = (IngredientList) args.getSerializable(INGREDIENTS);
        } else {
            mIngredientList = new IngredientList();
        }
        mViewAdapter = new IngredientItemRecyclerViewAdapter(this, mIngredientList, true);
        mRecyclerView.setAdapter(mViewAdapter);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menuItem_menuEdit_done) {
            onDoneClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onAddClick(View view) {
        if (mIngredientList.size() == 25)
            return;
        mIngredientList.addIngredient(new Ingredient(
                "",
                getResources().getStringArray(R.array.entries_spinner_edit_ingredients)[0],
                0));
        mViewAdapter.notifyItemInserted(mIngredientList.size()-1);
    }

    public void onDoneClick() {
        boolean isAllFilled = true;
        int ingredientListSize = mIngredientList.size();
        for (int i = 0; i < ingredientListSize; i++) {
            if (mIngredientList.getIngredient(i).getName().isEmpty()) {
                View v = mRecyclerView.getChildAt(i);
                RecyclerView.ViewHolder viewHolder = mRecyclerView.getChildViewHolder(v);
                mViewAdapter.setErrorOnEditText(viewHolder, "Should be filled");
                isAllFilled = false;
            }
        }
        if (!isAllFilled)
            return;

        Intent resultValue = new Intent();
        resultValue.putExtra(INGREDIENTS, mIngredientList);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}
