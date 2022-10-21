package com.SEG2505_Group8.mealer.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.SEG2505_Group8.mealer.Database.Models.MealerRole;
import com.SEG2505_Group8.mealer.Database.Models.MealerUser;
import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;
import com.SEG2505_Group8.mealer.UI.Activities.Utils.FieldValidator;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Presents form for creating a Chef account.
 */
public class ChefSignUpFormActivity extends AppCompatActivity {

    FieldValidator validator;

    EditText firstName;
    EditText lastName;
    EditText address;
    EditText email;
    EditText password;
    EditText description;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_sign_up_form);

        firstName = findViewById(R.id.chef_sign_up_form_first_name);
        lastName = findViewById(R.id.chef_sign_up_form_last_name);
        address = findViewById(R.id.chef_sign_up_form_address);
        email = findViewById(R.id.chef_sign_up_form_email);

        // Get email from Intent since user already filled this info in.
        email.setText(getIntent().getStringExtra("email"));

        password = findViewById(R.id.chef_sign_up_form_password);
        description = findViewById(R.id.chef_sign_up_form_description);

        submitButton = findViewById(R.id.chef_sign_up_form_submit_button);
        submitButton.setOnClickListener(view -> {
            if(validateFields()){
                submit(email.getText().toString(), password.getText().toString(), firstName.getText().toString(), lastName.getText().toString(), address.getText().toString(), description.getText().toString());
            }
        });

        validator = new FieldValidator(getApplicationContext());
    }

    /**
     * Submit form info to create a Chef account
     */
    public void submit(String email, String password, String firstName, String lastName, String address, String description) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {

                String userId;
                try {
                    userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                } catch (NullPointerException e) {
                    Toast.makeText(ChefSignUpFormActivity.this, "Failed to get user! Please restart app.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create user data in Database
                MealerUser user = new MealerUser(userId, MealerRole.CHEF, firstName, lastName, email, address, "", description, "voidCheckUrl");
                Services.getDatabaseClient().updateUser(user);

                // Send user back to Main. Restarts user experience flow.
                startActivity(new Intent(ChefSignUpFormActivity.this, MainActivity.class));
                finish();
            } else {
                // TODO: Handle failed account creation
                Toast.makeText(ChefSignUpFormActivity.this, "Failed to user account!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateFields() {
        return validator.required(firstName)
                && validator.required(lastName)
                && validator.required(email)
                && validator.email(email)
                && validator.required(password)
                && validator.password(password)
                && validator.required(description)
                && validator.required(address);
    }
}