package com.SEG2505_Group8.mealer.UI.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.SEG2505_Group8.mealer.Database.Models.MealerComplaint;
import com.SEG2505_Group8.mealer.Database.Models.MealerRecipe;
import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;
import com.SEG2505_Group8.mealer.UI.Activities.RecipeActivity;
import com.SEG2505_Group8.mealer.databinding.FragmentComplaintItemBinding;
import com.SEG2505_Group8.mealer.databinding.FragmentMenuItemBinding;
import com.google.android.material.chip.Chip;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MealerRecipe}.
 */
public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.ViewHolder> {

    private final List<MealerRecipe> mValues;

    public RecipeRecyclerViewAdapter(List<MealerRecipe> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentMenuItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.title.setText(holder.mItem.getName());
        holder.offeredChip.setVisibility(holder.mItem.getIsOffered() ? View.VISIBLE : View.GONE);

        holder.openButton.setOnClickListener(view -> {
            Intent i = new Intent(view.getContext(), RecipeActivity.class);
            i.putExtra("recipe", holder.mItem);
            view.getContext().startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public MealerRecipe mItem;
        public final Button openButton;
        public final TextView title;
        public final Chip offeredChip;

        public ViewHolder(FragmentMenuItemBinding binding) {
            super(binding.getRoot());

            openButton = binding.open;
            title = binding.menuItemTitle;
            offeredChip = binding.menuItemOfferedChip;
        }
    }
}