package com.SEG2505_Group8.mealer.UI.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.SEG2505_Group8.mealer.Database.Models.MealerComplaint;
import com.SEG2505_Group8.mealer.Database.Models.MealerOrder;
import com.SEG2505_Group8.mealer.Database.Models.MealerRecipe;
import com.SEG2505_Group8.mealer.Database.Utils.DatabaseListener;
import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;
import com.SEG2505_Group8.mealer.UI.Activities.Utils.FieldValidator;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;

/**
 * Presents form for creating a complaint.
 */
public class ComplaintFormActivity extends AppCompatActivity {

    String orderId;

    MealerOrder order;
    DatabaseListener listener;

    FieldValidator validator;
    EditText description;

    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_form);

        validator = new FieldValidator(getApplicationContext());

        orderId = getIntent().getStringExtra("orderId");

        if (orderId == null) {
            Toast.makeText(ComplaintFormActivity.this, "orderId not found!", Toast.LENGTH_SHORT).show();
        }

        description = findViewById(R.id.complaint_form_description);

        submitButton = findViewById(R.id.complaint_form_submit_button);
        submitButton.setOnClickListener(v -> {
            if (order != null && validateFields()) {
                submit(description.getText().toString());
            }
        });
    }

    /**
     * Submit form info to create a Chef account
     */
    private void submit(String description) {
        Services.getDatabaseClient().updateComplaint(new MealerComplaint(UUID.randomUUID().toString(), order.getChefId(), order.getClientId(), description), isSuccess -> {
            if (isSuccess) {
                Toast.makeText(ComplaintFormActivity.this, "Complaint submitted!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    /**
     * Assert all fields are valid
     * @return
     */
    private boolean validateFields() {
        return  validator.required(description);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (listener != null) {
            listener.remove();
        }

        listener = Services.getDatabaseClient().listenForModel(ComplaintFormActivity.this, "orders", orderId, MealerOrder.class, order -> {
            this.order = order;
        });
    }
}