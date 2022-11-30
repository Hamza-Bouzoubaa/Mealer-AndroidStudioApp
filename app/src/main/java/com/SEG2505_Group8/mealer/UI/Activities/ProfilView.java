package com.SEG2505_Group8.mealer.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;

public class ProfilView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_view);
    }
// test
    @Override
    protected void onResume() {
        super.onResume();

        Services.getDatabaseClient().getUser(user -> {
            user.getId();

        });
    }
}