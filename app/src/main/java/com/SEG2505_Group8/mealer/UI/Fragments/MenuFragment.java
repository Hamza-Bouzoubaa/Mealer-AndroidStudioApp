package com.SEG2505_Group8.mealer.UI.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.SEG2505_Group8.mealer.Database.Models.MealerComplaint;
import com.SEG2505_Group8.mealer.Database.Models.MealerMenu;
import com.SEG2505_Group8.mealer.Database.Models.MealerRecipe;
import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;
import com.SEG2505_Group8.mealer.UI.Adapters.ComplaintRecyclerViewAdapter;
import com.SEG2505_Group8.mealer.UI.Adapters.RecipeRecyclerViewAdapter;
import com.google.firebase.firestore.FieldPath;

/**
 * A fragment representing a list of Items.
 */
public class MenuFragment extends Fragment {

    View menuView;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MenuFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MenuFragment newInstance(int columnCount) {
        MenuFragment fragment = new MenuFragment();
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
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        menuView = view.findViewById(R.id.menu);

        // Set the adapter
        if (menuView instanceof RecyclerView) {
            Context context = menuView.getContext();
            RecyclerView recyclerView = (RecyclerView) menuView;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            Services.getDatabaseClient().getUser(user -> {
                Services.getDatabaseClient().listenForModel(getActivity(), "menus", user.getMenuId(), MealerMenu.class, menu -> {
                    Services.getDatabaseClient().listenForModels(getActivity(), "recipes", MealerRecipe.class, reference -> reference.whereIn(FieldPath.documentId(), menu.getRecipeIds()), recipes -> ((RecyclerView)menuView).setAdapter(new RecipeRecyclerViewAdapter(recipes)));
                });
            });
        }
        return view;
    }
}