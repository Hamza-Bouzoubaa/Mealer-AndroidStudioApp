package com.SEG2505_Group8.mealer.UI.Adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.SEG2505_Group8.mealer.Database.Models.MealerComplaint;
import com.SEG2505_Group8.mealer.Services;
import com.SEG2505_Group8.mealer.UI.Activities.ComplaintActivity;
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

        holder.openButton.setOnClickListener(view -> {
            Intent i = new Intent(view.getContext(), ComplaintActivity.class);
            i.putExtra("complaint", holder.mItem);
            view.getContext().startActivity(i);
        });
        holder.rejectButton.setOnClickListener(v -> {
            Services.getDatabaseClient().deleteComplaint(holder.mItem.getId(), isSuccessful -> {
                if (isSuccessful) {
                    mValues.remove(position);
                    notifyItemRemoved(position);
                    notifyItemChanged(position, mValues.size());
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final Button openButton;
        public final Button rejectButton;

        public MealerComplaint mItem;

        public ViewHolder(FragmentComplaintItemBinding binding) {
            super(binding.getRoot());

            openButton = binding.open;
            rejectButton = binding.reject;
        }
    }
}