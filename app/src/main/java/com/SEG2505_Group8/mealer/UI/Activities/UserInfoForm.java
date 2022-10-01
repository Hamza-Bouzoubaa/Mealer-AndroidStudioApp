package com.SEG2505_Group8.mealer.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.SEG2505_Group8.mealer.Database.Models.MealerRole;
import com.SEG2505_Group8.mealer.Database.Models.MealerUser;
import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class UserInfoForm extends AppCompatActivity {

    TextView firstName;
    TextView lastName;
    TextView address;
    TextView email;
    TextView creditCard;
    TextView description;

    CheckBox conditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_form);

        firstName = findViewById(R.id.user_input_form_first_name);
        lastName = findViewById(R.id.user_input_form_last_name);
        address = findViewById(R.id.user_info_form_address);
        email = findViewById(R.id.user_info_form_email);
        creditCard = findViewById(R.id.user_info_form_credit_card);
        description = findViewById(R.id.user_info_form_description);
        conditions = findViewById(R.id.user_info_form_conditions);
    }

    public void submitbuttonhandler(View view) {
        if (!conditions.isChecked()) {
            return;
        }

        List<Integer> ratings = new ArrayList<>();
        ratings.add(0);
        ratings.add(0);
        ratings.add(0);
        ratings.add(0);
        ratings.add(0);
        MealerUser user = new MealerUser(FirebaseAuth.getInstance().getCurrentUser().getUid(), firstName.getText().toString(), lastName.getText().toString(), email.getText().toString(), address.getText().toString(), creditCard.getText().toString(), "", "", description.getText().toString(), "", MealerRole.USER, ratings, 0);
        Services.getDatabaseClient().updateUser(user);

        // Create the MainActivity intent
        Intent i = new Intent(UserInfoForm.this, MainActivity.class);
        startActivity(i);

        // Kill our current intent
        finish();
    }
}