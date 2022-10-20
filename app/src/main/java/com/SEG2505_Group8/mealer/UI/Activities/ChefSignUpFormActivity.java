package com.SEG2505_Group8.mealer.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.SEG2505_Group8.mealer.Database.Models.MealerRole;
import com.SEG2505_Group8.mealer.Database.Models.MealerUser;
import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;
import com.google.firebase.auth.FirebaseAuth;

public class ChefSignUpFormActivity extends AppCompatActivity {

    EditText firstName;
    EditText lastName;
    EditText address;
    EditText email;
    EditText password;
    EditText description;
    Button submitButton;

    boolean isAllFieldsChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_sign_up_form);

        firstName = findViewById(R.id.chef_sign_up_form_first_name);
        lastName = findViewById(R.id.chef_sign_up_form_last_name);
        address = findViewById(R.id.chef_sign_up_form_address);
        email = findViewById(R.id.chef_sign_up_form_email);

        email.setText(getIntent().getStringExtra("email"));

        password = findViewById(R.id.chef_sign_up_form_password);
        description = findViewById(R.id.chef_sign_up_form_description);

        submitButton = findViewById(R.id.chef_sign_up_form_submit_button);
        submitButton.setOnClickListener(view -> {
            isAllFieldsChecked = CheckAllFields();

            if(isAllFieldsChecked){
                submit();
            }

        });
    }

    public void submit() {
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
                String validatedDescription = description.getText().toString();

                // Create user data in Database
                MealerUser user = new MealerUser(validatedId, MealerRole.CHEF, validatedFirstName, validatedLastName, validatedEmail, validatedAddress, "", validatedDescription, "voidCheckUrl");
                Services.getDatabaseClient().updateUser(user);

                startActivity(new Intent(ChefSignUpFormActivity.this, MainActivity.class));
                finish();
            } else {
                // Failed to create an account
                // TODO: Handle failed account creation
                Toast.makeText(ChefSignUpFormActivity.this, "Failed to user account!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean CheckAllFields() {
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

        if (description.length() == 0) {
            description.setError("This field is required");
            return false;
        }

        if (address.length() == 0) {
            address.setError("This field is required");
            return false;
        }

        // after all validation return true.
        return true;
    }
}