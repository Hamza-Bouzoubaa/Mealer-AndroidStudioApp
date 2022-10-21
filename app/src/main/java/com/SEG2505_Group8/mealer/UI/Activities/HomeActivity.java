package com.SEG2505_Group8.mealer.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.SEG2505_Group8.mealer.Database.Callbacks.DatabaseCompletionCallback;
import com.SEG2505_Group8.mealer.Database.Models.MealerRecipe;
import com.SEG2505_Group8.mealer.Database.Models.MealerUser;
import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Welcome Activity for sign in users.
 * Presents current status and a logout button.
 */
public class HomeActivity extends AppCompatActivity {

    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        welcomeText = findViewById(R.id.displayUser);

//        Button deleteRecipe = findViewById(R.id.btnDeleteRecipe);
//        deleteRecipe.setOnClickListener(v -> {
//            Services.getDatabaseClient().deleteRecipe("recipe1");
//        });

        // button for logout and initialing our button.
        Button logoutBtn = findViewById(R.id.idBtnLogout);

        // adding onclick listener for our logout button.
        logoutBtn.setOnClickListener(v -> {

            // Call Firebase Authentication sign out
            AuthUI.getInstance()
                    .signOut(HomeActivity.this)

                    // Redirect user to MainActivity
                    .addOnCompleteListener(task -> {

                        // Tell user they were signed out
                        Toast.makeText(HomeActivity.this, "User Signed Out", Toast.LENGTH_SHORT).show();

                        // Go to Main Activity
                        Intent i = new Intent(HomeActivity.this, MainActivity.class);
                        startActivity(i);

                        // Kill current activity since we won't be back without going trough sign in
                        finish();
                    });
        });

        try {
            System.out.println("user: " + FirebaseAuth.getInstance().getCurrentUser().getUid() + " is logged in.");
        } catch (Exception e) {
            System.out.println("Failed to get current user.");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Listen for current user's name and role
        Services.getDatabaseClient().listenForModel(this, "users", FirebaseAuth.getInstance().getCurrentUser().getUid(), MealerUser.class, user -> {

            String text = "Bienvenue "+ user.getFirstName()  + "! Vous êtes connecté en tant que: ";
            String role = "null";
            switch (user.getRole()){
                case CHEF:
                    role = getString(R.string.role_chef);
                    break;
                case USER:
                    role = getString(R.string.role_user);
                    break;
                case ADMIN:
                    role = getString(R.string.role_admin);
                    break;
            }

            text += role.substring(0,1).toUpperCase() + role.substring(1);

            welcomeText.setText(text);
        });
    }
}