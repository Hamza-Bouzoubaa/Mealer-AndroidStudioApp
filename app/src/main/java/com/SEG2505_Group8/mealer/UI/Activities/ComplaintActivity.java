package com.SEG2505_Group8.mealer.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
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
    CheckBox isPermanent;

    Date endDateData = new Date();

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

        isPermanent = findViewById(R.id.complaint_permanent);

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

                if (o != null) {
                    o.suspend(isPermanent.isChecked() ? null : endDateData);
                    Services.getDatabaseClient().updateUser(o);
                }
                Services.getDatabaseClient().deleteComplaint(complaint.getId(), success -> {
                    finish();
                });
            });
        });
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        endDateData = new Date(year, month, day);
        endDate.setText(endDateData.toString());
    }
}