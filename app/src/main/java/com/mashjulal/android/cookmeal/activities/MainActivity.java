package com.mashjulal.android.cookmeal.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.mashjulal.android.cookmeal.R;
import com.mashjulal.android.cookmeal.fragments.RecipeBookFragment;
import com.mashjulal.android.cookmeal.fragments.SearchRecipesFragment;
import com.mashjulal.android.cookmeal.fragments.TimerFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private FragmentManager mFragmentManager;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_aMain);
        mToolbar.setTitle(getString(R.string.main_title));
        setSupportActionBar(mToolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout_aMain);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView_aMain);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_recipe_book);
        mFragmentManager = getSupportFragmentManager();
        Fragment fragment = RecipeBookFragment.newInstance();
        changeFragment(fragment);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.INTERNET,
                        Manifest.permission_group.CAMERA,
                        Manifest.permission_group.STORAGE},
                1);

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CropImageActivity.class)));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout_aMain);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_recipe_book) {
            fragment = RecipeBookFragment.newInstance();
        } else if (id == R.id.nav_search_recipes) {
            fragment = SearchRecipesFragment.newInstance();
        }  else if (id == R.id.nav_timer) {
            fragment = TimerFragment.newInstance();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout_aMain);
        drawer.closeDrawer(GravityCompat.START);
        changeFragment(fragment);
        mToolbar.setTitle(item.getTitle());
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void changeFragment(Fragment fragment) {
        mFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout_aMain_container, fragment)
                .commit();
    }
}
