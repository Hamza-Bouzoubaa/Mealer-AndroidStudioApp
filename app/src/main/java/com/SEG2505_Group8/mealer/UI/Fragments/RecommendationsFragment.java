package com.SEG2505_Group8.mealer.UI.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.SEG2505_Group8.mealer.Database.Models.MealerRecipe;
import com.SEG2505_Group8.mealer.Database.Utils.DatabaseListener;
import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;
import com.SEG2505_Group8.mealer.UI.Adapters.RecommendationRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class RecommendationsFragment extends Fragment {

    SearchView search;
    RecyclerView results;

    DatabaseListener listener;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecommendationsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RecommendationsFragment newInstance(int columnCount) {
        RecommendationsFragment fragment = new RecommendationsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recommendations, container, false);

        results = v.findViewById(R.id.recommendation_list);

        // Set the adapter
        if (results != null) {
            Context context = results.getContext();
            RecyclerView recyclerView = results;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
        }

        search = v.findViewById(R.id.recommendation_search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                Services.getDatabaseClient().searchRecipesByName(query, 10, recipes -> {
                    if (recipes == null) {
                        recipes = new ArrayList<>();
                    }
                    results.setAdapter(new RecommendationRecyclerViewAdapter(recipes));
                });

                return true;
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (listener != null) {
            listener.remove();
        }

        // Listen for orders from Database
        listener = Services.getDatabaseClient().listenForModels(getActivity(), "recipes", MealerRecipe.class, reference -> reference.whereEqualTo("isOffered", true).limit(10), recipes -> {
            results.setAdapter(new RecommendationRecyclerViewAdapter(recipes));
        });
    }
}