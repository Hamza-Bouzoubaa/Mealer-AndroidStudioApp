package com.SEG2505_Group8.mealer.UI.Activities;


import androidx.appcompat.app.AppCompatActivity;

import com.SEG2505_Group8.mealer.Database.FirebaseDatabaseClient;
import com.SEG2505_Group8.mealer.Database.Models.MealerComplaint;
import com.SEG2505_Group8.mealer.Database.Models.MealerOrder;
import com.SEG2505_Group8.mealer.Services;

import android.os.Bundle;
import android.widget.ProgressBar;

import com.SEG2505_Group8.mealer.R;



public class OrderPageActivity extends AppCompatActivity {

    ProgressBar deliveryProgress;
    MealerOrder order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_page);

//        deliveryProgress.findViewById(R.id.deliveryProgress);
//        deliveryProgress.setProgress(0);

        order = (MealerOrder) getIntent().getSerializableExtra("order");

    }
}