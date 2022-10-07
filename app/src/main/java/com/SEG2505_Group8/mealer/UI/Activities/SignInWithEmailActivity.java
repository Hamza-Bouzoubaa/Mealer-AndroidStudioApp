package com.SEG2505_Group8.mealer.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.SEG2505_Group8.mealer.R;

public class SignInWithEmailActivity extends AppCompatActivity {

    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_with_email);

        submitButton = findViewById(R.id.sign_in_with_email_continue_button);

        submitButton.setOnClickListener(view -> {
            ActivityUtils.launchActivity(this, SignInWithEmailActivity.this, MainActivity.class);
        });

        // If the user presses the back button on their phone
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Create the MainActivity intent
                Intent i = new Intent(SignInWithEmailActivity.this, SignInActivity.class);
                startActivity(i);

                // Kill our current intent
                finish();
            }
        });
    }
}
