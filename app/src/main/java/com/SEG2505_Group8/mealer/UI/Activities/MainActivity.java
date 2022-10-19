package com.SEG2505_Group8.mealer.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.SEG2505_Group8.mealer.Database.Models.MealerRecipe;
import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button login;

    // variable for Firebase Auth
    // see: https://www.geeksforgeeks.org/how-to-use-firebase-ui-authentication-library-in-android/
    private FirebaseAuth mFirebaseAuth;

    // creating an auth listener for our Firebase auth
    private FirebaseAuth.AuthStateListener mAuthStateListner;

    /**
     * Launch home activity unless user info is required.
     */
    private void tryLaunchHomeActivity() {
        //TODO: Implement check for new user info required?
        startActivity(new Intent(MainActivity.this, HomeActivity.class));

        //Kill main activity since user shouldn't come back using back button.
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.login);

        login.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
        });

        mFirebaseAuth = FirebaseAuth.getInstance();

        // checking if the user is null or not.
        if (mFirebaseAuth.getCurrentUser() != null) {
            tryLaunchHomeActivity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}

