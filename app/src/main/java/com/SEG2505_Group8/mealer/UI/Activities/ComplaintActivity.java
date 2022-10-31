package com.SEG2505_Group8.mealer.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.SEG2505_Group8.mealer.Database.Models.MealerComplaint;
import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.UI.Fragments.DatePickerFragment;

public class ComplaintActivity extends AppCompatActivity {

    MealerComplaint complaint;
    TextView complaintDescription;
    Button suspendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        complaint = (MealerComplaint) getIntent().getSerializableExtra("complaint");
        System.out.println("Deserialized complaint");

        complaintDescription = findViewById(R.id.complaint_description);

        complaintDescription.setText(complaint.getDescription());

        suspendButton = findViewById(R.id.complaint_date);
        suspendButton.setOnClickListener(view -> {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
        });
    }
}