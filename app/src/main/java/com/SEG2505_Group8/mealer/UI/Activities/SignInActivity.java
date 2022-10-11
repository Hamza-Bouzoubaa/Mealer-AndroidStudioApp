package com.SEG2505_Group8.mealer.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.SEG2505_Group8.mealer.R;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    EditText email;
    Button emailContinueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email = findViewById(R.id.sign_in_email_field);
        emailContinueButton = findViewById(R.id.sign_in_continue_button);

        emailContinueButton.setOnClickListener(view -> {
            // TODO: Check if email is already associated with an account

            FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email.getText().toString()).addOnCompleteListener(task -> {

                boolean isNewUser = true;

                try {
                    isNewUser = task.getResult().getSignInMethods().isEmpty();
                } catch (Exception e) {

                }

                if (isNewUser) {
                    // Create the UserInfoForm intent
                    Intent i = new Intent(SignInActivity.this, UserInfoForm.class);
                    startActivity(i);

                    // Kill our current intent
                    finish();
                } else {
                    // Create the SignInWithEmail intent
                    Intent i = new Intent(SignInActivity.this, SignInWithEmailActivity.class);
                    startActivity(i);

                    // Kill our current intent
                    finish();
                }
            });
        });

        // If the user presses the back button on their phone
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Create the MainActivity intent
                Intent i = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(i);

                // Kill our current intent
                finish();
            }
        });
    }
}
