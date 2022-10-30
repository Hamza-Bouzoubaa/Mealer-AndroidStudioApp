package com.SEG2505_Group8.mealer.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.SEG2505_Group8.mealer.Database.Models.MealerComplaint;
import com.SEG2505_Group8.mealer.R;

public class ComplaintActivity extends AppCompatActivity {

    MealerComplaint complaint;
    TextView complaintDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        complaint = (MealerComplaint) getIntent().getSerializableExtra("complaint");
        System.out.println("Deserialized complaint");

        complaintDescription = findViewById(R.id.complaint_description);

        complaintDescription.setText(complaint.getDescription());
    }
}