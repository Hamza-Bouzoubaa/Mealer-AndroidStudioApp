package com.SEG2505_Group8.mealer.UI.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.SEG2505_Group8.mealer.Database.Models.MealerOrder;
import com.SEG2505_Group8.mealer.Database.Models.MealerOrderStatus;
import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;
import com.SEG2505_Group8.mealer.UI.Activities.OrderPageActivity;
import com.SEG2505_Group8.mealer.databinding.FragmentOrderItemBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MealerOrder}.
 */
public class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.ViewHolder> {

    private final List<MealerOrder> mValues;

    public OrderRecyclerViewAdapter(List<MealerOrder> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentOrderItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.title.setText("Order: " + holder.mItem.getId());

        holder.openButton.setOnClickListener(view -> {
            Intent i = new Intent(view.getContext(), OrderPageActivity.class);
            i.putExtra("orderId", holder.mItem.getId());
            view.getContext().startActivity(i);
        });

        if (holder.mItem.getStatus().equals(MealerOrderStatus.REJECTED)) {
            holder.rejectButton.setText(R.string.delete);
        }

        if (holder.mItem.getClientId().equals(FirebaseAuth.getInstance().getUid())) {
            holder.rejectButton.setText(R.string.cancel);
        }

        holder.rejectButton.setOnClickListener(v -> {

            if (holder.mItem.getStatus().equals(MealerOrderStatus.REJECTED)) {
                Services.getDatabaseClient().deleteOrder(holder.mItem.getId(), isSuccessful -> {
                    if (isSuccessful) {
                        Toast.makeText(v.getContext(), "Order deleted", Toast.LENGTH_SHORT).show();
                        mValues.remove(position);
                        notifyItemRemoved(position);
                        notifyItemChanged(position, mValues.size());
                    }
                });
            } else {
                Services.getDatabaseClient().rejectOrder(holder.mItem, v.getContext(), isSuccessful -> {
                    if (isSuccessful) {
                        Toast.makeText(v.getContext(), "Order rejected", Toast.LENGTH_SHORT).show();
                        mValues.remove(position);
                        notifyItemRemoved(position);
                        notifyItemChanged(position, mValues.size());
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final Button openButton;
        public final Button rejectButton;
        public final TextView title;

        public MealerOrder mItem;

        public ViewHolder(FragmentOrderItemBinding binding) {
            super(binding.getRoot());

            openButton = binding.open;
            rejectButton = binding.reject;
            title = binding.orderItemTitle;
        }
    }
}