package com.SEG2505_Group8.mealer.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.SEG2505_Group8.mealer.Database.Models.MealerUser;
import com.SEG2505_Group8.mealer.Database.Utils.DatabaseListener;
import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;

public class ProfileActivity extends AppCompatActivity {

    String userId;
    DatabaseListener listener;

    TextView firstName;
    TextView lastName;
    TextView email;
    TextView address;
    TextView description;
    ImageView[] stars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        userId = getIntent().getStringExtra("userId");
        if (userId == null) {
            Toast.makeText(ProfileActivity.this, "Failed to get user id!", Toast.LENGTH_SHORT).show();
            finish();
        }

        firstName = findViewById(R.id.profile_first_name);
        lastName = findViewById(R.id.profile_last_name);
        email = findViewById(R.id.profile_email);
        address = findViewById(R.id.profile_address);
        description = findViewById(R.id.profile_description);

        stars = new ImageView[5];
        stars[0] = findViewById(R.id.profile_1_star);
        stars[1] = findViewById(R.id.profile_2_star);
        stars[2] = findViewById(R.id.profile_3_star);
        stars[3] = findViewById(R.id.profile_4_star);
        stars[4] = findViewById(R.id.profile_5_star);

        Button closeButton = findViewById(R.id.profile_close);
        closeButton.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (listener != null) {
            listener.remove();
        }

        listener = Services.getDatabaseClient().listenForModel(ProfileActivity.this, "users", userId, MealerUser.class, user -> {
            firstName.setText(user.getFirstName());
            lastName.setText(user.getLastName());
            email.setText(user.getEmail());
            address.setText(user.getAddress());
            description.setText(user.getDescription());

            float r = user.averageRating();

            for (int i = 0; i < stars.length; i++) {
                stars[i].setImageResource(i+1 <= r ? R.drawable.ic_baseline_star_24 : R.drawable.ic_baseline_star_border_24);
            }
        });
    }
}