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
import com.SEG2505_Group8.mealer.Database.Models.MealerOrder;
import com.SEG2505_Group8.mealer.Database.Models.MealerOrderStatus;
import com.SEG2505_Group8.mealer.Database.Models.MealerOrderStatusUtils;
import com.SEG2505_Group8.mealer.Database.Models.MealerRole;
import com.SEG2505_Group8.mealer.Database.Utils.DatabaseListener;
import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;
import com.SEG2505_Group8.mealer.UI.Adapters.ComplaintRecyclerViewAdapter;
import com.SEG2505_Group8.mealer.UI.Adapters.OrderRecyclerViewAdapter;
import com.SEG2505_Group8.mealer.UI.Adapters.RecipeRecyclerViewAdapter;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

/**
 * A fragment representing a list of Items.
 */
public class OrderListFragment extends Fragment {

    DatabaseListener listener;

    View orderView;

    Chip showResolvedChip;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OrderListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static OrderListFragment newInstance(int columnCount) {
        OrderListFragment fragment = new OrderListFragment();
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
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);

        orderView = view.findViewById(R.id.order_list);
        showResolvedChip = view.findViewById(R.id.order_list_chips_offered);

        showResolvedChip.setCheckable(true);

        showResolvedChip.setOnCheckedChangeListener((v, checked) -> {
            listenForOrders(checked);
        });

        // Set the adapter
        if (orderView instanceof RecyclerView) {
            Context context = orderView.getContext();
            RecyclerView recyclerView = (RecyclerView) orderView;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        listenForOrders(showResolvedChip.isChecked());
    }

    @Override
    public void onPause() {
        super.onPause();
        listener.remove();
    }

    private void listenForOrders(boolean showResolved) {
        if (listener != null) {
            listener.remove();
        }

        Services.getDatabaseClient().getUser(u -> {
            listener = Services.getDatabaseClient().listenForModels(getActivity(), "orders", MealerOrder.class, reference -> {

                Query q = reference.whereEqualTo(u.getRole().equals(MealerRole.CHEF) ? "chefId" : "clientId", u.getId());

                if (!showResolved) {
                   q = q.whereNotIn("status", MealerOrderStatusUtils.getHiddenOrderStatus());
                }

                return q.limit(100);
            }, orders -> {
                // Give updated orders to adapter
                ((RecyclerView)orderView).setAdapter(new OrderRecyclerViewAdapter(orders));
            });
        });
    }
}