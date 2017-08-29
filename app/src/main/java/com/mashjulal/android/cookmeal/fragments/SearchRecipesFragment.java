package com.mashjulal.android.cookmeal.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mashjulal.android.cookmeal.R;


public class SearchRecipesFragment extends Fragment {

    public static final String TAG = SearchRecipesFragment.class.getSimpleName();

    public SearchRecipesFragment() {
        // Required empty public constructor
    }

    public static SearchRecipesFragment newInstance() {
        SearchRecipesFragment fragment = new SearchRecipesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_recipes, container, false);
    }
}
