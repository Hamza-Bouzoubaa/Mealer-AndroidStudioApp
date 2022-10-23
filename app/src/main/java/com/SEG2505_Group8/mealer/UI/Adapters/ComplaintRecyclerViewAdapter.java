package com.SEG2505_Group8.mealer.UI.Adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.SEG2505_Group8.mealer.Database.Models.MealerComplaint;
import com.SEG2505_Group8.mealer.databinding.FragmentComplaintItemBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MealerComplaint}.
 */
public class ComplaintRecyclerViewAdapter extends RecyclerView.Adapter<ComplaintRecyclerViewAdapter.ViewHolder> {

    private final List<MealerComplaint> mValues;

    public ComplaintRecyclerViewAdapter(List<MealerComplaint> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentComplaintItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.chefIdView.setText(mValues.get(position).getChefId());
        holder.userIdView.setText(mValues.get(position).getUserId());
        holder.descriptionView.setText(mValues.get(position).getDescription());
        holder.actionButton.setOnClickListener(v -> System.out.println("Clicked action " + holder.descriptionView.getText()));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView chefIdView;
        public final TextView userIdView;
        public final TextView descriptionView;
        public final Button actionButton;

        public MealerComplaint mItem;

        public ViewHolder(FragmentComplaintItemBinding binding) {
            super(binding.getRoot());

            chefIdView = binding.chefId;
            userIdView = binding.userId;
            descriptionView = binding.description;
            actionButton = binding.action;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + chefIdView.getText() + ", " + userIdView.getText() + ", " + descriptionView.getText() + "'";
        }
    }
}