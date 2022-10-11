package com.SEG2505_Group8.mealer.UI.Activities;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.SEG2505_Group8.mealer.R;
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
//        try {
        ActivityUtils.launchActivity(this, MainActivity.this, HomeActivity.class);
//            if (Services.getDatabaseClient().userInfoRequired().get()) {
//                //TODO: Implement get new user info
////                ActivityUtils.launchActivity(this, MainActivity.this, SignInActivity.class);
//            } else {
//                ActivityUtils.launchActivity(this, MainActivity.this, HomeActivity.class);
//            }
//        } catch (ExecutionException | InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.login);

        login.setOnClickListener(view -> {
            ActivityUtils.launchActivity(this, MainActivity.this, SignInActivity.class);
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

