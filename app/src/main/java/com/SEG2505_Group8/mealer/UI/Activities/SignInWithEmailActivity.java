package com.SEG2505_Group8.mealer.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.SEG2505_Group8.mealer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInWithEmailActivity extends AppCompatActivity {

    TextView emailView;
    EditText passwordField;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_with_email);

        emailView = findViewById(R.id.sign_in_with_email_email);
        submitButton = findViewById(R.id.sign_in_with_email_continue_button);

        passwordField = findViewById(R.id.sign_in_email_password);
        passwordField.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                submitButton.callOnClick();
                handled = true;
            }
            return handled;
        });

        emailView.setText(getIntent().getStringExtra("email"));

        submitButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(getIntent().getStringExtra("email"), passwordField.getText().toString()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    // Create the MainActivity intent
                    Intent i = new Intent(SignInWithEmailActivity.this, MainActivity.class);
                    startActivity(i);

                    // Kill our current intent
                    finish();
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(SignInWithEmailActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            });
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
