package com.SEG2505_Group8.mealer.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.SEG2505_Group8.mealer.R;

public class ChooseUserTypeActivity extends AppCompatActivity {

    ImageButton userButton;
    ImageButton chefButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user_type);

        userButton = findViewById(R.id.choose_user_type_user_button);
        chefButton = findViewById(R.id.choose_user_type_chef_button);

        userButton.setOnClickListener(view -> {
            Intent i = new Intent(ChooseUserTypeActivity.this, UserSignUpFormActivity.class);
            i.putExtra("email", getIntent().getStringExtra("email"));
            startActivity(i);
        });
        chefButton.setOnClickListener(view -> {
            Intent i = new Intent(ChooseUserTypeActivity.this, ChefSignUpFormActivity.class);
            i.putExtra("email", getIntent().getStringExtra("email"));
            startActivity(i);
        });
    }
}