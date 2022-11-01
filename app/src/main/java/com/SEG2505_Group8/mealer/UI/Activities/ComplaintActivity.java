package com.SEG2505_Group8.mealer.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.SEG2505_Group8.mealer.Database.Models.MealerComplaint;
import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;
import com.SEG2505_Group8.mealer.UI.Activities.Utils.DateUtils;
import com.SEG2505_Group8.mealer.UI.Fragments.DatePickerFragment;

import java.util.Date;

public class ComplaintActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener{

    MealerComplaint complaint;
    TextView complaintDescription;
    TextView endDate;

    Date endDateData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        complaint = (MealerComplaint) getIntent().getSerializableExtra("complaint");
        System.out.println("Deserialized complaint");

        complaintDescription = findViewById(R.id.complaint_description);
        complaintDescription.setText(complaint.getDescription());

        endDate = findViewById(R.id.complaint_end_date);
        endDate.setText(DateUtils.nowPrettyString());

        Button setEndButton = findViewById(R.id.complaint_date);
        setEndButton.setOnClickListener(view -> {
            DialogFragment newFragment = new DatePickerFragment(this);
            newFragment.show(getSupportFragmentManager(), "datePicker");
        });

        Button cancelButton = findViewById(R.id.complaint_cancel);
        cancelButton.setOnClickListener(view -> {
            finish();
        });

        Button submitButton = findViewById(R.id.complaint_submit);
        submitButton.setOnClickListener(view -> {
            Services.getDatabaseClient().getUser(complaint.getChefId(), o -> {
                o.suspend(endDateData);
                Services.getDatabaseClient().updateUser(o);
            });
        });
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        endDateData = new Date(year, month, day);
        endDate.setText(endDateData.toString());
    }
}