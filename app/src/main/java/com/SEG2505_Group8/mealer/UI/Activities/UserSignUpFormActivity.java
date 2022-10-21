package com.SEG2505_Group8.mealer.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.SEG2505_Group8.mealer.Database.Models.MealerRole;
import com.SEG2505_Group8.mealer.Database.Models.MealerUser;
import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;
import com.google.firebase.auth.FirebaseAuth;

public class UserSignUpFormActivity extends AppCompatActivity {

    EditText firstName;
    EditText lastName;
    EditText address;
    EditText email;
    EditText password;
    EditText creditCard;
    EditText description;

    CheckBox conditions;

    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up_form);

        firstName = findViewById(R.id.user_sign_up_form_first_name);
        lastName = findViewById(R.id.user_sign_up_form_last_name);
        address = findViewById(R.id.user_sign_up_form_address);
        email = findViewById(R.id.user_sign_up_form_email);

        email.setText(getIntent().getStringExtra("email"));

        password = findViewById(R.id.user_sign_up_form_password);
        creditCard = findViewById(R.id.user_sign_up_form_credit_card);
        description = findViewById(R.id.user_sign_up_form_description);
        conditions = findViewById(R.id.user_sign_up_form_conditions);

        submitButton = findViewById(R.id.user_sign_up_form_submit_button);
        submitButton.setOnClickListener(view -> {
            if (validateFields()) {
                submit(
                        email.getText().toString(),
                        password.getText().toString(),
                        firstName.getText().toString(),
                        lastName.getText().toString(),
                        address.getText().toString(),
                        creditCard.getText().toString(),
                        description.getText().toString()
                );
            }
        });
    }

    public void submit(String email, String password, String firstName, String lastName, String address, String creditCard, String description) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                String userId;
                try {
                    userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                } catch (NullPointerException e) {
                    Toast.makeText(UserSignUpFormActivity.this, "Failed to get user! Please restart the app.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create user data in Database
                MealerUser user = new MealerUser(userId, MealerRole.USER, firstName, lastName, email, address, creditCard, description, "");
                Services.getDatabaseClient().updateUser(user);

                // Go to MainActivity, restart login flow.
                startActivity(new Intent(UserSignUpFormActivity.this, MainActivity.class));
                finish();
            } else {
                // Failed to create an account
                // TODO: Handle failed account creation
                Toast.makeText(UserSignUpFormActivity.this, "Failed to create user account!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateFields() {
        if (firstName.length() == 0) {
            firstName.setError(getString(R.string.form_required_field));
            return false;
        }

        if (lastName.length() == 0) {
            lastName.setError(getString(R.string.form_required_field));
            return false;
        }

        // Check email using Regex to make sure it follows the format <x>@<y>.<z>
        if (email.length() == 0) {
            email.setError(getString(R.string.form_required_field));
            return false;
        }else if (!email.getText().toString().trim().matches("[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+")) {
            email.setError(getString(R.string.form_invalid_email));
            return false;
        }

        // Password must be at least 8 characters long
        if (password.length() == 0) {
            password.setError(getString(R.string.form_required_field));
            return false;
        } else if (password.length() < 8) {
            password.setError(getString(R.string.form_invalid_password));
            return false;
        }

        // Credit card number must be at least 16 digits long
        if (creditCard.length() == 0) {
            creditCard.setError(getString(R.string.form_required_field));
            return false;
        }else if (creditCard.length() < 16) {
            creditCard.setError(getString(R.string.form_invalid_credit_card));
            return false;
        }

        if (description.length() == 0) {
            description.setError(getString(R.string.form_required_field));
            return false;
        }

        if (address.length() == 0) {
            address.setError(getString(R.string.form_required_field));
            return false;
        }

        if (!conditions.isChecked()) {
            conditions.setError(getString(R.string.form_invalid_terms_and_conditions));
            return false;
        }

        // If all validations pass, return true
        return true;
    }
}