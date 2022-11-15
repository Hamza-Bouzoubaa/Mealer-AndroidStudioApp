package com.SEG2505_Group8.mealer.UI.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.SEG2505_Group8.mealer.Database.Models.MealerComplaint;
import com.SEG2505_Group8.mealer.Database.Models.MealerRecipe;
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

    FieldValidator validator;
    EditText description;

    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_form);

        validator = new FieldValidator(getApplicationContext());
    }

    /**
     * Submit form info to create a Chef account
     */
    private void submit(String description) {
        Services.getDatabaseClient().updateComplaint(new MealerComplaint(UUID.randomUUID().toString(), getIntent().getStringExtra("ChefId"), getIntent().getStringExtra("UserId"), description));
    }

    /**
     * Assert all fields are valid
     * @return
     */
    private boolean validateFields() {
        return  validator.required(description);
    }
}