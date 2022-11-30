package com.SEG2505_Group8.mealer.UI.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.SEG2505_Group8.mealer.Database.Models.MealerOrder;
import com.SEG2505_Group8.mealer.Database.Models.MealerRecipe;
import com.SEG2505_Group8.mealer.Services;
import com.SEG2505_Group8.mealer.UI.Activities.ComplaintActivity;
import com.SEG2505_Group8.mealer.UI.Activities.RecipeActivity;
import com.SEG2505_Group8.mealer.UI.Activities.RecipeClientActivity;
import com.SEG2505_Group8.mealer.databinding.FragmentOrderItemBinding;
import com.SEG2505_Group8.mealer.databinding.FragmentRecommendationItemBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link com.SEG2505_Group8.mealer.Database.Models.MealerRecipe}.
 */
public class RecommendationRecyclerViewAdapter extends RecyclerView.Adapter<RecommendationRecyclerViewAdapter.ViewHolder> {

    private final List<MealerRecipe> mValues;

    public RecommendationRecyclerViewAdapter(List<MealerRecipe> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentRecommendationItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.title.setText(holder.mItem.getName());

        holder.openButton.setOnClickListener(view -> {
            Intent i = new Intent(view.getContext(), RecipeClientActivity.class);
            i.putExtra("recipe", holder.mItem);
            view.getContext().startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final Button openButton;
        public final TextView title;

        public MealerRecipe mItem;

        public ViewHolder(FragmentRecommendationItemBinding binding) {
            super(binding.getRoot());

            openButton = binding.open;
            title = binding.recommendationItemTitle;
        }
    }
}