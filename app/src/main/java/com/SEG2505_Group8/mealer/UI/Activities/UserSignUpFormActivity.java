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

import com.SEG2505_Group8.mealer.Database.Models.MealerClient;
import com.SEG2505_Group8.mealer.Database.Models.MealerRole;
import com.SEG2505_Group8.mealer.Database.Models.MealerUser;
import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;
import com.SEG2505_Group8.mealer.UI.Activities.Utils.FieldValidator;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Presents form for creating a User account.
 */
public class UserSignUpFormActivity extends AppCompatActivity {

    FieldValidator validator;

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

        // Present previously inputted email
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

        validator = new FieldValidator(getApplicationContext());
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
                MealerUser user = new MealerClient(userId, firstName, lastName, email, address, creditCard, description);
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

    /**
     * Assert all fields are valid
     * @return
     */
    private boolean validateFields() {
        return validator.required(firstName)
                && validator.required(lastName)
                && validator.required(email)
                && validator.email(email)
                && validator.required(password)
                && validator.password(password)
                && validator.required(description)
                && validator.creditCard(creditCard)
                && validator.required(address)
                && validator.termsAndConditions(conditions);
    }
}