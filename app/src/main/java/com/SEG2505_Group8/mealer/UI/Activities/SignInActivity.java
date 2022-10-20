package com.SEG2505_Group8.mealer.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.SEG2505_Group8.mealer.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Presents sign in methods to user.
 * Forwards user to {@link ChooseUserTypeActivity} if their email is not recognized.
 */
public class SignInActivity extends AppCompatActivity {


    EditText email;
    Button emailContinueButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email = findViewById(R.id.sign_in_email_field);
        email.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                emailContinueButton.callOnClick();
                handled = true;
            }
            return handled;
        });

        emailContinueButton = findViewById(R.id.sign_in_continue_button);
        emailContinueButton.setOnClickListener(view -> {
            // TODO: Add Gini's field validation
            redirectUser(email.getText().toString());
        });
    }

    /**
     * Redirects user to proper activity based off of their email.
     * If their email is in the system, prompt for their password.
     * Else, present account creation.
     * @param email
     */
    public void redirectUser(String email) {

        if (email == null || email.isEmpty()) {
            return;
        }

        FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {

            boolean isNewUser = true;

            // Check if Firebase Auth has sign in methods. If their are none, then the account does not exist.
            try {
                isNewUser = task.getResult().getSignInMethods().isEmpty();
            } catch (Exception e) {
                System.out.println("Failed to get SignInMethods. Presenting account creation.");
            }

            Intent i = new Intent(SignInActivity.this, isNewUser ? ChooseUserTypeActivity.class : SignInWithEmailActivity.class);

            // Package email inside intent to reuse in following views.
            i.putExtra("email", email);
            startActivity(i);
        });
    }
}
