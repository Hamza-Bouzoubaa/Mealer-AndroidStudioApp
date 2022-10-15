package com.SEG2505_Group8.mealer.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.SEG2505_Group8.mealer.Database.Models.MealerRole;
import com.SEG2505_Group8.mealer.Database.Models.MealerUser;
import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class UserInfoForm extends AppCompatActivity {

    EditText firstName;
    EditText lastName;
    EditText address;
    EditText email;
    EditText password;
    EditText creditCard;
    EditText description;

    CheckBox conditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_form);

        firstName = findViewById(R.id.user_input_form_first_name);
        lastName = findViewById(R.id.user_input_form_last_name);
        address = findViewById(R.id.user_info_form_address);
        email = findViewById(R.id.user_info_form_email);
        password = findViewById(R.id.user_info_form_password);
        creditCard = findViewById(R.id.user_info_form_credit_card);
        description = findViewById(R.id.user_info_form_description);
        conditions = findViewById(R.id.user_info_form_conditions);

        // If the user presses the back button on their phone, go back to Main
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Create the MainActivity intent
                Intent i = new Intent(UserInfoForm.this, MainActivity.class);
                startActivity(i);

                // Kill our current intent
                finish();
            }
        });
    }

    public void submitbuttonhandler(View view) {
        if (!conditions.isChecked()) {
            return;
        }

        String validatedEmail = email.getText().toString();
        String validatedPassword = password.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(validatedEmail, validatedPassword).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                // Account created

                // Validate fields TODO: Add validation
                String validatedId;
                try {
                    validatedId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                } catch (NullPointerException e) {
                    // TODO: Handle invalid data in user creation
                    return;
                }

                String validatedFirstName = firstName.getText().toString();
                String validatedLastName = lastName.getText().toString();
                String validatedAddress = address.getText().toString();
                String validatedCreditCard = creditCard.getText().toString();
                String validatedDescription = description.getText().toString();

                // Create user data in Database
                MealerUser user = new MealerUser(validatedId, validatedFirstName, validatedLastName, validatedEmail, validatedAddress, validatedCreditCard, validatedDescription);
                Services.getDatabaseClient().updateUser(user);

                // Create the MainActivity intent
                Intent i = new Intent(UserInfoForm.this, MainActivity.class);
                startActivity(i);

                // Kill our current intent
                finish();
            } else {
                // Failed to create an account
                // TODO: Handle failed account creation
                Toast.makeText(UserInfoForm.this, "Failed to user account!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}