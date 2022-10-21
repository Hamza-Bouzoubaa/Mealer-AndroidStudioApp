package com.SEG2505_Group8.mealer.UI.Activities.Utils;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.EditText;

import com.SEG2505_Group8.mealer.R;

public class FieldValidator {

    Context context;

    private static String emailRegex = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

    public FieldValidator(Context context) {
        this.context = context;
    }

    public boolean required(EditText text) {
        if (text.length() == 0) {
            text.setError(context.getString(R.string.form_required_field));
            return false;
        }

        return true;
    }

    public boolean password(EditText text) {
        if (text.length() < 8) {
            text.setError(context.getString(R.string.form_invalid_password));
            return false;
        }

        return true;
    }

    public boolean email(EditText text) {
        if (!text.getText().toString().matches(emailRegex)) {
            text.setError(context.getString(R.string.form_invalid_email));
            return false;
        }

        return true;
    }

    public boolean termsAndConditions(CheckBox checkBox) {
        if (!checkBox.isChecked()) {
            checkBox.setError(context.getString(R.string.form_invalid_terms_and_conditions));
            return false;
        }

        return true;
    }

    public boolean creditCard(EditText text) {
        if (text.length() < 16) {
            text.setError(context.getString(R.string.form_invalid_credit_card));
            return false;
        }

        return true;
    }
}
