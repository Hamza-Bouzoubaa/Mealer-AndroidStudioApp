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

/**
 * Sign in using email and password.
 * On success, send user to {@link HomeActivity}
 */
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

        // Listen for Enter key. On press, submit email and password.
        passwordField.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                submitButton.callOnClick();
                handled = true;
            }
            return handled;
        });

        // Present email that was previously inputted by user
        emailView.setText(getIntent().getStringExtra("email"));

        submitButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(getIntent().getStringExtra("email"), passwordField.getText().toString()).addOnCompleteListener(task -> {
                // If account creation was successful, go to HomeActivity
                if (task.isSuccessful()) {
                    startActivity(new Intent(SignInWithEmailActivity.this, HomeActivity.class));
                    finish();
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(SignInWithEmailActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
