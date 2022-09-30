package com.SEG2505_Group8.mealer.UI.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    // variable for Firebase Auth
    // see: https://www.geeksforgeeks.org/how-to-use-firebase-ui-authentication-library-in-android/
    private FirebaseAuth mFirebaseAuth;

    // creating an auth listener for our Firebase auth
    private FirebaseAuth.AuthStateListener mAuthStateListner;

    // below is the list which we have created in which
    // we can add the authentication which we have to
    // display inside our app.
    List<AuthUI.IdpConfig> providers = Arrays.asList(

            // below is the line for adding
            // email and password authentication.
            new AuthUI.IdpConfig.EmailBuilder().build(),

            // below line is used for adding GitHub
            // authentication builder in our app.
            new AuthUI.IdpConfig.GitHubBuilder().build()
    );


    private ActivityResultLauncher<Intent> signInLauncher;

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            tryLaunchHomeActivity();
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }

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

        signInLauncher = registerForActivityResult(
                new FirebaseAuthUIActivityResultContract(),
                result -> {
                    onSignInResult((FirebaseAuthUIAuthenticationResult) result);
                }
        );

        // below line is for getting instance
        // for our firebase auth
        mFirebaseAuth = FirebaseAuth.getInstance();

        // below line is used for calling auth listener
        // for oue Firebase authentication.
        mAuthStateListner = firebaseAuth -> {

            // we are calling method for on authentication state changed.
            // below line is used for getting current user which is
            // authenticated previously.
            FirebaseUser user = firebaseAuth.getCurrentUser();

            // checking if the user
            // is null or not.
            if (user != null) {
                tryLaunchHomeActivity();
            } else {
                // this method is called when our
                // user is not authenticated previously.

                signInLauncher.launch(
                        // below line is used for getting
                        // our authentication instance.
                        AuthUI.getInstance()
                                // below line is used to
                                // create our sign in intent
                                .createSignInIntentBuilder()

                                // below line is used for adding smart
                                // lock for our authentication.
                                // smart lock is used to check if the user
                                // is authentication through different devices.
                                // currently we are disabling it.
                                .setIsSmartLockEnabled(false)

                                // we are adding different login providers which
                                // we have mentioned above in our list.
                                // we can add more providers according to our
                                // requirement which are available in firebase.
                                .setAvailableProviders(providers)

                                // below line is for customizing our theme for
                                // login screen and set logo method is used for
                                // adding logo for our login page.
                                .setLogo(R.drawable.gfgimage).setTheme(R.style.LoginTheme)

                                // after setting our theme and logo
                                // we are calling a build() method
                                // to build our login screen.
                                .build()
                );
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        // we are calling our auth
        // listener method on app resume.
        mFirebaseAuth.addAuthStateListener(mAuthStateListner);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // here we are calling remove auth
        // listener method on stop.
        mFirebaseAuth.removeAuthStateListener(mAuthStateListner);
    }
}

