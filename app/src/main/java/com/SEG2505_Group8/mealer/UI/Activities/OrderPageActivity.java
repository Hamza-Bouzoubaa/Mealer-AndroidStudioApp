package com.SEG2505_Group8.mealer.UI.Activities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.SEG2505_Group8.mealer.Database.FirebaseDatabaseClient;
import com.SEG2505_Group8.mealer.Database.MealerMessagingService;
import com.SEG2505_Group8.mealer.Database.Models.MealerComplaint;
import com.SEG2505_Group8.mealer.Database.Models.MealerOrder;
import com.SEG2505_Group8.mealer.Database.Models.MealerOrderStatus;
import com.SEG2505_Group8.mealer.Database.Models.MealerOrderStatusUtils;
import com.SEG2505_Group8.mealer.Database.Models.MealerRole;
import com.SEG2505_Group8.mealer.Database.Utils.DatabaseListener;
import com.SEG2505_Group8.mealer.Database.Utils.MealerMessager;
import com.SEG2505_Group8.mealer.Services;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.SEG2505_Group8.mealer.R;
import com.google.firebase.auth.FirebaseAuth;


public class OrderPageActivity extends AppCompatActivity {

    MealerOrder order;

    TextView title;
    TextView status;
    TextView recipeName;

    Spinner statusSpinner;

    ProgressBar progress;
    String orderId;

    DatabaseListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_page);

        status = findViewById(R.id.order_status_status_text);
        recipeName = findViewById(R.id.order_status_recipe_name);


        progress = findViewById(R.id.order_status_progress);
        progress.setMin(0);
        progress.setMax(MealerOrderStatusUtils.getMaxStatusStep());

        statusSpinner = findViewById(R.id.order_status_select);

        Button closeButton = findViewById(R.id.order_status_close);
        closeButton.setOnClickListener(v -> {
            finish();
        });

        orderId = getIntent().getStringExtra("orderId");

        Services.getDatabaseClient().getUser(user -> {

            ConstraintLayout variableViewChef = findViewById(R.id.order_status_variable_chef);
            ConstraintLayout variableViewClient = findViewById(R.id.order_status_variable_client);

            variableViewChef.setVisibility(user.getRole() == MealerRole.USER ? View.GONE : View.VISIBLE);
            variableViewClient.setVisibility(user.getRole() == MealerRole.USER ? View.VISIBLE : View.GONE);

            statusSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, MealerOrderStatus.values()));

            Button saveButton = findViewById(R.id.order_status_save);
            saveButton.setVisibility(user.getRole() == MealerRole.USER ? View.GONE : View.VISIBLE);
            saveButton.setOnClickListener(v -> {

                MealerOrderStatus newStatus = MealerOrderStatus.valueOf(statusSpinner.getSelectedItem().toString());
                boolean dirtyStatus = order.getStatus() != newStatus;


                if (dirtyStatus) {
                    order.setStatus(newStatus);
                }
                Services.getDatabaseClient().updateOrder(order, isSuccessful -> {

                    if (isSuccessful && dirtyStatus) {
                        Services.getDatabaseClient().getUser(order.getClientId(), u -> {
                            if (u != null) {
                                MealerMessager messager = new MealerMessager(getApplicationContext());
                                messager.notifyClientOrder(order.getId(), u.getMessageToken());
                            }

                            Toast.makeText(OrderPageActivity.this, "Updated order!", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    }
                });
            });

        });

        listenForOrder();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listenForOrder();
    }

    private void listenForOrder() {

        if (orderId == null || orderId.equals("")) {
            return;
        }

        if (listener != null) {
            listener.remove();
        }

        Services.getDatabaseClient().listenForModel(OrderPageActivity.this, "orders", orderId, MealerOrder.class, order -> {
            if (order == null) {
                return;
            }

            this.order = order;

            progress.setProgress(order.getStatusStep());
            status.setText(getString(R.string.order_status_text, order.getStatus()));

            if (order.getRecipeId() != null) {
                Services.getDatabaseClient().getRecipe(order.getRecipeId(), recipe -> {
                    recipeName.setText(recipe.getName());
                });
            }

            statusSpinner.setSelection(order.getStatus().ordinal());

            Button complaintButton = findViewById(R.id.order_status_write_complaint);
            complaintButton.setVisibility(View.GONE);
            complaintButton.setOnClickListener(v -> {
                Intent i = new Intent(OrderPageActivity.this, ComplaintFormActivity.class);
                i.putExtra("orderId", order.getId());
                startActivity(i);
            });

            if (order.getClientId() != null && FirebaseAuth.getInstance().getUid() != null) {
                complaintButton.setVisibility(FirebaseAuth.getInstance().getUid().equals(order.getClientId()) ? View.VISIBLE : View.GONE);
            }
        });
    }
}