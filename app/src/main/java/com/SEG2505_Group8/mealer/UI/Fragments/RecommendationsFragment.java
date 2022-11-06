package com.SEG2505_Group8.mealer.UI.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.SEG2505_Group8.mealer.Database.Models.MealerRecipe;
import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;
import com.SEG2505_Group8.mealer.UI.Activities.RecipeActivity;

/*
 * A simple {@link Fragment} subclass.
 * Use the {@link RecommendationsFragment} factory method to
 * create an instance of this fragment.
 */
public class RecommendationsFragment extends Fragment {

    public RecommendationsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recommendations, container, false);
        Button b = v.findViewById(R.id.recommendations_open_recipe);
        b.setOnClickListener(view -> {
            Services.getDatabaseClient().getRecipe("recipe1", object -> {
                Intent i = new Intent(getContext(), RecipeActivity.class);
                i.putExtra("recipe", object);
                startActivity(i);
            });
        });
        return v;
    }
}