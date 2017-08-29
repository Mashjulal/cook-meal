package com.mashjulal.android.cookmeal.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.mashjulal.android.cookmeal.database.CookMealDBSchema.DATABASE_NAME;
import static com.mashjulal.android.cookmeal.database.CookMealDBSchema.IngredientListsTable;
import static com.mashjulal.android.cookmeal.database.CookMealDBSchema.IngredientsTable;
import static com.mashjulal.android.cookmeal.database.CookMealDBSchema.RecipesTable;
import static com.mashjulal.android.cookmeal.database.CookMealDBSchema.StepListsTable;
import static com.mashjulal.android.cookmeal.database.CookMealDBSchema.StepsTable;


class CookMealDB {

    private static final String TAG = CookMealDB.class.getSimpleName();

    private static final int VERSION = 1;

    private static final String CREATE_TABLE_RECIPES =
            "CREATE TABLE IF NOT EXISTS " + RecipesTable.TABLE_NAME + " (" +
            RecipesTable.Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            RecipesTable.Columns.NAME + " TEXT, " +
            RecipesTable.Columns.PREVIEW_IMAGE_PATH + " TEXT);";

    private static final String CREATE_TABLE_STEP_LISTS =
            "CREATE TABLE IF NOT EXISTS " + StepListsTable.TABLE_NAME + " (" +
                    StepListsTable.Columns.RECIPE_ID + " INTEGER, " +
                    StepListsTable.Columns.STEP_ID + " INTEGER);";

    private static final String CREATE_TABLE_STEPS =
            "CREATE TABLE IF NOT EXISTS " + StepsTable.TABLE_NAME + " (" +
                    StepsTable.Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    StepsTable.Columns.POSITION + " INTEGER," +
                    StepsTable.Columns.IMAGE_PATH + " TEXT, " +
                    StepsTable.Columns.DESCRIPTION + " TEXT, " +
                    StepsTable.Columns.TIME + " INTEGER);";

    private static final String CREATE_TABLE_INGREDIENT_LISTS =
            "CREATE TABLE IF NOT EXISTS " + IngredientListsTable.TABLE_NAME + " (" +
                    IngredientListsTable.Columns.RECIPE_ID + " INTEGER, " +
                    IngredientListsTable.Columns.INGREDIENT_ID + " INTEGER);";

    private static final String CREATE_TABLE_INGREDIENTS =
            "CREATE TABLE IF NOT EXISTS " + IngredientsTable.TABLE_NAME + " (" +
                    IngredientsTable.Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    IngredientsTable.Columns.NAME + " TEXT, " +
                    IngredientsTable.Columns.TYPE + " TEXT, " +
                    IngredientsTable.Columns.VALUE + " INTEGER);";

    private Context mContext;

    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public CookMealDB(Context context) {
        mContext = context;
    }

    public void open(){
        mDBHelper = new DBHelper(mContext, DATABASE_NAME, null, VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    public void close(){
        if (mDBHelper != null)
            mDBHelper.close();
    }

    public Cursor getRecipesCursor(){
        return mDB.query(
                CookMealDBSchema.RecipesTable.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    public Cursor getRecipeCursor(int recipeID){
        return mDB.query(
                CookMealDBSchema.RecipesTable.TABLE_NAME,
                null,
                CookMealDBSchema.RecipesTable.Columns.ID + "=?",
                new String[] {String.valueOf(recipeID)},
                null,
                null,
                null);
    }

    public Cursor getIngredientsCursor(int recipeID){
        String query = String.format(
                "SELECT * FROM %s WHERE %s IN (SELECT %s FROM %s WHERE %s = %s);",
                CookMealDBSchema.IngredientsTable.TABLE_NAME,
                CookMealDBSchema.IngredientsTable.Columns.ID,
                CookMealDBSchema.IngredientListsTable.Columns.INGREDIENT_ID,
                CookMealDBSchema.IngredientListsTable.TABLE_NAME,
                CookMealDBSchema.IngredientListsTable.Columns.RECIPE_ID,
                recipeID);
        return mDB.rawQuery(query, null);
    }

    public Cursor getStepsCursor(int recipeID){
        String query = String.format(
                "SELECT * FROM %s WHERE %s IN (SELECT %s FROM %s WHERE %s = %s);",
                StepsTable.TABLE_NAME,
                StepsTable.Columns.ID,
                StepListsTable.Columns.STEP_ID,
                StepListsTable.TABLE_NAME,
                StepListsTable.Columns.RECIPE_ID,
                recipeID);
        return mDB.rawQuery(query, null);
    }

    public Cursor getTimeCursor(int recipeID) {
        String query = String.format(
                "SELECT %s FROM %s WHERE %s IN (SELECT %s FROM %s WHERE %s = %s);",
                StepsTable.Columns.TIME,
                StepsTable.TABLE_NAME,
                StepsTable.Columns.ID,
                StepListsTable.Columns.STEP_ID,
                StepListsTable.TABLE_NAME,
                StepListsTable.Columns.RECIPE_ID,
                recipeID);
        return mDB.rawQuery(query, null);
    }

    public long addData(ContentValues cv, String tableName){
        return mDB.insert(tableName, null, cv);
    }

    public void deleteData(int id, String tableName, String tableID){
        mDB.delete(tableName, tableID + " = " + id, null);
    }

    public Cursor getData(String whereClause, String[] whereArgs, String tableName){
        return mDB.query(tableName, null, whereClause, whereArgs, null, null, null);
    }

    private class DBHelper extends SQLiteOpenHelper {
        DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // create required tables
            db.execSQL(CREATE_TABLE_RECIPES);
            db.execSQL(CREATE_TABLE_STEP_LISTS);
            db.execSQL(CREATE_TABLE_STEPS);
            db.execSQL(CREATE_TABLE_INGREDIENT_LISTS);
            db.execSQL(CREATE_TABLE_INGREDIENTS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
