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

import com.SEG2505_Group8.mealer.Database.Models.MealerRecipe;
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

public class HomeActivity extends AppCompatActivity {

    //private static final String TAG = "HomeActivity";

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = auth.getCurrentUser();  //creation d'un utilisateur avec info the firebase
    String uid = currentUser.getUid();                  // chercher l'identification du user

    private FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
    private DocumentReference usersInfo = dataBase.collection("users").document(uid);
    private TextView textViewData;


    List<MealerRecipe> spotlightRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        textViewData = findViewById(R.id.displayNameHome);





        //String collectionTypeUsers = "users";
        //String getInfoOfTypeFirstName = "firstName";








        Button deleteRecipe = findViewById(R.id.btnDeleteRecipe);
        deleteRecipe.setOnClickListener(v -> {
            Services.getDatabaseClient().deleteRecipe("recipe1");
        });

        // button for logout and initialing our button.
        Button logoutBtn = findViewById(R.id.idBtnLogout);

        // adding onclick listener for our logout button.
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // below line is for getting instance
                // for AuthUi and after that calling a
                // sign out method from FIrebase.
                AuthUI.getInstance()
                        .signOut(HomeActivity.this)

                        // after sign out is executed we are redirecting
                        // our user to MainActivity where our login flow is being displayed.
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {

                                // below method is used after logout from device.
                                Toast.makeText(HomeActivity.this, "User Signed Out", Toast.LENGTH_SHORT).show();

                                // below line is to go to MainActivity via an intent.
                                Intent i = new Intent(HomeActivity.this, MainActivity.class);
                                startActivity(i);
                            }
                        });
            }
        });
        Services.getDatabaseClient().updateRecipe(new MealerRecipe("recipe1", "Chips", "Appetizer", null, null, null, 10.25f, "Some chips with ketchup"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        usersInfo.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if(e != null){
                    Toast.makeText(HomeActivity.this, "error loading", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(documentSnapshot.exists()){
                    String firstName = documentSnapshot.getString("firstName");
                    String userRole = documentSnapshot.getString("role");
                    textViewData.setText("firstName"+ firstName + "/n" + "role: "+ userRole);
                }
            }
        });
    }

    public void loadUser(View v){

        usersInfo.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            String firstName = documentSnapshot.getString("firstName");
                            String userRole = documentSnapshot.getString("role");
                            textViewData.setText("firstName"+ firstName + "/n" + "role: "+ userRole);
                        }else{
                            Toast.makeText(HomeActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HomeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                    }
                });
    }


}