package com.SEG2505_Group8.mealer.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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

    /**
     * Launch form to collect more info about the user.
     * Only call if the user is authenticated with Firebase Auth.
     */
    private void launchUserInfoFormActivity() {

        // Create the UserInfoForm intent
        Intent i = new Intent(MainActivity.this, UserInfoForm.class);
        startActivity(i);

        // Kill our current intent
        finish();
    }

    /**
     * Only call if the user is authenticated with Firebase Auth
     */
    private void launchHomeActivity() {

        // Create the HomeActivity intent
        Intent i = new Intent(MainActivity.this, HomeActivity.class);
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
                launchUserInfoFormActivity();
            } else {
                launchHomeActivity();
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


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchUserInfoFormActivity();
                //TODO: add new activity << chef or client>>

            }
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
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
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
                    }
                });
    }
}

