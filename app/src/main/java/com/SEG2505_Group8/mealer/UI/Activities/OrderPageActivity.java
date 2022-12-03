package com.SEG2505_Group8.mealer.UI.Activities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

    // Array holding the 5 stars for ratings. Idx 0 = 1 star review. idx 4 = 5 star review.
    ImageButton[] stars = new ImageButton[5];
    Integer rating = null;

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

        stars[0] = findViewById(R.id.order_status_1_star);
        stars[1] = findViewById(R.id.order_status_2_star);
        stars[2] = findViewById(R.id.order_status_3_star);
        stars[3] = findViewById(R.id.order_status_4_star);
        stars[4] = findViewById(R.id.order_status_5_star);

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

                    if (isSuccessful) {
                        Services.getDatabaseClient().getUser(order.getClientId(), u -> {
                            if (u != null) {

                                if (dirtyStatus) {
                                    MealerMessager messager = new MealerMessager(getApplicationContext());
                                    messager.notifyClientOrder(order.getId(), u.getMessageToken());
                                }
                            }
                        });
                    }
                });
            });

        });

        for (int i = 0; i < stars.length; i++) {
            int finalI = i;
            stars[i].setOnClickListener(v -> {
                clickStar(finalI);
            });
        }

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
            status.setText(getString(R.string.order_status_text, MealerOrderStatusUtils.getPrettyStatus(order.getStatus(), this)));

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
                LinearLayout linear = findViewById(R.id.order_status_stars);
                linear.setVisibility(FirebaseAuth.getInstance().getUid().equals(order.getClientId()) ? View.VISIBLE : View.GONE);
            }

        });
    }

    public void clickStar(int idx) {

        rating = idx;

        for (int i = 0; i < stars.length; i++) {
            stars[i].setImageResource(i <= idx ? R.drawable.ic_baseline_star_24 : R.drawable.ic_baseline_star_border_24);
        }

        if (rating != null) {
            Services.getDatabaseClient().getRecipe(order.getRecipeId(), recipe -> {
                recipe.rate(rating, order.getClientId());

                Services.getDatabaseClient().updateRecipe(recipe, isSuccess -> {
                    Services.getDatabaseClient().getUser(order.getChefId(), chef -> {
                        chef.rate(rating, order.getClientId());
                        Services.getDatabaseClient().updateUser(chef, s -> {
                            Toast.makeText(OrderPageActivity.this, "Updated rating!", Toast.LENGTH_SHORT).show();
                        });
                    });
                });
            });
        }
    }
}