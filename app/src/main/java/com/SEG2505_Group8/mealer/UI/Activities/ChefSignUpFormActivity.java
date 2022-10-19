package com.SEG2505_Group8.mealer.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.SEG2505_Group8.mealer.R;

public class ChefSignUpFormActivity extends AppCompatActivity {

    EditText firstName;
    EditText lastName;
    EditText address;
    EditText email;
    EditText password;
    Button voidCheckButton;
    EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_sign_up_form);
    }
}