package com.SEG2505_Group8.mealer.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private Button register;
    private Button login;

    // variable for Firebase Auth
    // see: https://www.geeksforgeeks.org/how-to-use-firebase-ui-authentication-library-in-android/
    private FirebaseAuth mFirebaseAuth;

    // creating an auth listener for our Firebase auth
    private FirebaseAuth.AuthStateListener mAuthStateListner;

    private void launchActivity(Class<?> activity) {
        // Create the intent
        Intent i = new Intent(MainActivity.this, activity);
        startActivity(i);

        // Kill our current intent
        finish();
    }

    /**
     * Launch home activity unless user info is required.
     */
    private void tryLaunchHomeActivity() {
        try {
            if (Services.getDatabaseClient().userInfoRequired().get()) {
                //TODO: Implement get new user info
                launchActivity(HomeActivity.class);
            } else {
                launchActivity(HomeActivity.class);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = findViewById(R.id.register);
        login = findViewById(R.id.login);


        register.setOnClickListener(view -> {
            launchActivity(UserInfoForm.class);
            //TODO: add new activity << chef or client>>

        });

        login.setOnClickListener(view -> {
            launchActivity(SignInActivity.class);
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


    private void OnClickSubmitNewAccount() {

        String email = "";
        String password = "";

        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mFirebaseAuth.getCurrentUser();
                        tryLaunchHomeActivity();
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        //TODO: Implement failed sign up
                    }
                });
    }
}

