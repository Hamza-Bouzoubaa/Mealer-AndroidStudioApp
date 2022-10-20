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

    boolean FieldsCheck = false;

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
            FieldsCheck= CheckFields();
            if(FieldsCheck){
                submit();
            }
        });
    }

    public void submit() {
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
                MealerUser user = new MealerUser(validatedId, MealerRole.USER, validatedFirstName, validatedLastName, validatedEmail, validatedAddress, validatedCreditCard, validatedDescription, "");
                Services.getDatabaseClient().updateUser(user);

                startActivity(new Intent(UserSignUpFormActivity.this, MainActivity.class));
                finish();
            } else {
                // Failed to create an account
                // TODO: Handle failed account creation
                Toast.makeText(UserSignUpFormActivity.this, "Failed to user account!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean CheckFields() {
        String val = email.getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if (firstName.length() == 0) {
            firstName.setError("This field is required");
            return false;
        }

        if (lastName.length() == 0) {
            lastName.setError("This field is required");
            return false;
        }


        if (email.length() == 0) {
            email.setError("Email is required");
            return false;
        }else if (!val.matches(checkEmail)) {
            email.setError("Invalid Email!");
            return false;
        }


        if (password.length() == 0) {
            password.setError("Password is required");
            return false;
        } else if (password.length() < 8) {
            password.setError("Password must be minimum 8 characters");
            return false;
        }

        if (creditCard.length() == 0) {
            creditCard.setError("This field is required");
            return false;
        }else if (creditCard.length() < 16) {
            creditCard.setError("Credit card number must be minimum of 16 digits");
            return false;
        }

        if (description.length() == 0) {
            description.setError("This field is required");
            return false;
        }

        if (address.length() == 0) {
            address.setError("This field is required");
            return false;
        }

        if (!conditions.isChecked()) {
            conditions.setError("Terms and conditions must be accepted first");
            return false;
        }



        // after all validation return true.
        return true;
    }
}