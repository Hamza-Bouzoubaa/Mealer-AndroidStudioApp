package com.SEG2505_Group8.mealer.UI.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.SEG2505_Group8.mealer.Database.Models.MealerRole;
import com.SEG2505_Group8.mealer.Database.Models.MealerUser;
import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;
import com.google.firebase.auth.FirebaseAuth;
import android.widget.ImageView;

/**
 * Presents form for creating a Chef account.
 */
public class ChefSignUpFormActivity extends AppCompatActivity {

    EditText firstName;
    EditText lastName;
    EditText address;
    EditText email;
    EditText password;
    Button voidCheckButton;
    EditText description;
    Button submitButton;
    private ImageView imageView;
    private Button button;


    public static final int RequestPermissionCode = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_sign_up_form);
        imageView= findViewById(R.id.capturedimage);
        button=findViewById(R.id.chef_sign_up_form_void_check);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open_camera= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(open_camera,100);
            }
        });




        firstName = findViewById(R.id.chef_sign_up_form_first_name);
        lastName = findViewById(R.id.chef_sign_up_form_last_name);
        address = findViewById(R.id.chef_sign_up_form_address);
        email = findViewById(R.id.chef_sign_up_form_email);

        // Get email from Intent since user already filled this info in.
        email.setText(getIntent().getStringExtra("email"));

        password = findViewById(R.id.chef_sign_up_form_password);
        voidCheckButton = findViewById(R.id.chef_sign_up_form_void_check);
        description = findViewById(R.id.chef_sign_up_form_description);

        submitButton = findViewById(R.id.chef_sign_up_form_submit_button);
        submitButton.setOnClickListener(view -> {
            submit();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap photo= (Bitmap)data.getExtras().get("data");
        imageView.setImageBitmap(photo);
    }

    /**
     * Submit form info to create a Chef account
     */
    public void submit() {
        String validatedEmail = email.getText().toString();
        String validatedPassword = password.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(validatedEmail, validatedPassword).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {

                // Validate fields TODO: Add validation
                String validatedId;
                try {
                    validatedId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                } catch (NullPointerException e) {
                    // TODO: Handle invalid data in user creation
                    return;
                }

                String validatedFirstName = firstName.getText().toString();
                String validatedLastName = lastName.getText().toString();
                String validatedAddress = address.getText().toString();
                String validatedDescription = description.getText().toString();

                // Create user data in Database
                MealerUser user = new MealerUser(validatedId, MealerRole.CHEF, validatedFirstName, validatedLastName, validatedEmail, validatedAddress, "", validatedDescription, "voidCheckUrl");
                Services.getDatabaseClient().updateUser(user);

                // Send user back to Main. Restarts user experience flow.
                startActivity(new Intent(ChefSignUpFormActivity.this, MainActivity.class));
                finish();
            } else {
                // TODO: Handle failed account creation
                Toast.makeText(ChefSignUpFormActivity.this, "Failed to user account!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}