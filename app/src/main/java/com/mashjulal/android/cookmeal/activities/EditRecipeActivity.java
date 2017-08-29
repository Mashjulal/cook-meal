package com.mashjulal.android.cookmeal.activities;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ikovac.timepickerwithseconds.MyTimePickerDialog;
import com.mashjulal.android.cookmeal.R;
import com.mashjulal.android.cookmeal.adapters.RecipeStepCardRecyclerViewAdapter;
import com.mashjulal.android.cookmeal.database.CookMealLab;
import com.mashjulal.android.cookmeal.objects.IngredientList;
import com.mashjulal.android.cookmeal.objects.Recipe;
import com.mashjulal.android.cookmeal.objects.Step;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.mashjulal.android.cookmeal.Utils.getPathFromUri;
import static com.mashjulal.android.cookmeal.Utils.getTime;
import static com.mashjulal.android.cookmeal.activities.CreateIngredientsActivity.INGREDIENTS;
import static com.mashjulal.android.cookmeal.fragments.RecipeBookFragment.REQUEST_CODE_RECIPE_ADDED;

public class EditRecipeActivity extends AppCompatActivity {

    public static final String TAG = EditRecipeActivity.class.getSimpleName();

    public static final String PARAM_PAGE_URL = "page_url";

    private static final int SELECT_PICTURE = 1;
    private static final int TAKE_PHOTO = 4;
    private static final int REQUEST_CODE_INGREDIENTS = 6;

    private Recipe mRecipe;
    private Dialog mBottomSheetDialog;
    private TextInputLayout mTextInputLayoutName;
    private RecyclerView mRecyclerViewRecipeSteps;
    private RecipeStepCardRecyclerViewAdapter mViewAdapter;
    private LinearLayoutManager mLayoutManager;
    private FloatingActionButton mFabAddStep;
    private RelativeLayout mLayout;

    private String mLastPhotoPath;
    private int mLastPosition;

    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView,
                                      RecyclerView.ViewHolder viewHolder,
                                      RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    mViewAdapter.removeItem(viewHolder.getAdapterPosition());
                    mFabAddStep.setVisibility(View.VISIBLE);
        }});

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);
        mRecipe = new Recipe();
        mLayout = (RelativeLayout) findViewById(R.id.rl_aEditRecipe_layout);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewRecipeSteps = (RecyclerView) findViewById(R.id.rv_iEditRecipe_stepCards);
        mViewAdapter = new RecipeStepCardRecyclerViewAdapter(this, mRecipe.getStepsList(), true);
        mViewAdapter.setOnButtonClickListener(new RecipeStepCardRecyclerViewAdapter.OnButtonClickListener() {
            @Override
            public void onAddImageClick(int position) {
                mLastPosition = position;
                showBottomSheetDialogImage();
            }

            @Override
            public void onAddTimeClick(final int position) {
                final Step step = mRecipe.getStepsList().get(position);
                int[] timeArray = getTime(step.getTime());
                MyTimePickerDialog tpd = new MyTimePickerDialog(EditRecipeActivity.this,
                        (view, hourOfDay, minute, seconds) -> {
                            long tm = (seconds + minute * 60 + hourOfDay * 3600) * 1000;
                            step.setTime(tm);
                            mViewAdapter.notifyItemChanged(position);
                        },  timeArray[0], timeArray[1], timeArray[2], true);
                tpd.show();
            }
        });
        mRecyclerViewRecipeSteps.setAdapter(mViewAdapter);
        itemTouchHelper.attachToRecyclerView(mRecyclerViewRecipeSteps);
        mRecyclerViewRecipeSteps.setLayoutManager(mLayoutManager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_aEditRecipe);
        toolbar.setTitle(getString(R.string.title_create_recipe));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBottomSheetDialog = new Dialog(this);
        mTextInputLayoutName = (TextInputLayout) findViewById(R.id.til_iRecipeStepEditCard_description);
        mFabAddStep = (FloatingActionButton) findViewById(R.id.fab_aEditRecipe_addStep);
        mFabAddStep.setOnClickListener(v -> addStep());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.mi_menuEditRecipe_done:
                done();
                return true;
            case R.id.mi_menuEditRecipe_ingredientList:
                openIngredientsActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri dataUri;
            Step step;
            switch (requestCode) {
                case SELECT_PICTURE:
                    dataUri = data.getData();
                    step = mRecipe.getStepsList().get(mLastPosition);
                    step.setImagePath(getPathFromUri(this, dataUri));
                    mViewAdapter.notifyItemChanged(mLastPosition);
                    break;
                case TAKE_PHOTO:
                    step = mRecipe.getStepsList().get(mLastPosition);
                    step.setImagePath(mLastPhotoPath);
                    mViewAdapter.notifyItemChanged(mLastPosition);
                    break;
                case REQUEST_CODE_INGREDIENTS:
                    mRecipe.setIngredientList(
                            (IngredientList) data.getSerializableExtra(INGREDIENTS));
                    break;
            }
        }
    }

    private void addStep() {
        int index = mRecipe.getStepsList().size();
        if (index == 24) {
            mFabAddStep.setVisibility(View.GONE);
        } else if (index >= 25) {
            return;
        }
        mViewAdapter.addItem(new Step());
        mLayoutManager.scrollToPosition(index);
    }

    private void done() {
        if (!isAllFilled())
            return;
        saveToDB();
        setResult(REQUEST_CODE_RECIPE_ADDED);
        finish();
    }

    private boolean isAllFilled() {
        boolean isAllDone = true;

        String recipeName = mTextInputLayoutName.getEditText().getText().toString();
        mRecipe.setName(recipeName);
        if (recipeName.isEmpty()) {
            mTextInputLayoutName.setError("Should be filled!");
            isAllDone = false;
        }
        if (mRecipe.getStepsList().isEmpty()) {
            Snackbar.make(mLayout, "Add steps!", Snackbar.LENGTH_LONG)
                        .show();
            return false;
        }

        for (int i = 0; i < mRecipe.getStepsList().size(); i++) {
            Step step = mRecipe.getStepsList().get(i);
            // Set step number
            step.setStepNumber(i+1);
            // Check if step description isn't empty, if empty - set error to it's edit text
            String desc = step.getDescription();
            if (desc.isEmpty()) {
                View v = mRecyclerViewRecipeSteps.getChildAt(i);
                RecyclerView.ViewHolder viewHolder = mRecyclerViewRecipeSteps.getChildViewHolder(v);
                mViewAdapter.setErrorOnEditText(viewHolder, "Should be filled!");
                isAllDone = false;
            }

        }

        IngredientList ingredientList = mRecipe.getIngredientList();
        if (ingredientList.isEmpty()) {
            Snackbar.make(mLayout, "Add ingredients!", Snackbar.LENGTH_LONG)
                    .setAction("Add", v -> openIngredientsActivity())
                    .show();
            isAllDone = false;
        }

        return isAllDone;
    }

    private void saveToDB() {
        CookMealLab.getInstance(this).addRecipe(mRecipe);
    }

    public void openIngredientsActivity() {
        Intent intent = new Intent(this, CreateIngredientsActivity.class);
        if (!mRecipe.getIngredientList().isEmpty())
            intent.putExtra(INGREDIENTS, mRecipe.getIngredientList());
        startActivityForResult(intent, REQUEST_CODE_INGREDIENTS);
    }

    private void showBottomSheetDialogImage() {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog_image, null);
        LinearLayout llSelectFromGallery =
                (LinearLayout) view.findViewById(R.id.linearLayout_bsdImage_selectFromGallery);
        LinearLayout llTakePhoto =
                (LinearLayout) view.findViewById(R.id.linearLayout_bsdImage_takePhoto);

        llSelectFromGallery.setOnClickListener(v -> {
            mBottomSheetDialog.cancel();
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,
                    "Select Picture"), SELECT_PICTURE);
        });
        llTakePhoto.setOnClickListener(v -> {
            mBottomSheetDialog.cancel();
            takePicture();
        });

        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity (Gravity.BOTTOM);
        mBottomSheetDialog.show();
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(TAG, ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.mashjulal.android.cookmeal.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PNG_" + timeStamp + "_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        storageDir.mkdirs();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mLastPhotoPath = image.getAbsolutePath();
        return image;
    }
}
