package com.SEG2505_Group8.mealer.UI.Activities.Utils;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.EditText;

import com.SEG2505_Group8.mealer.R;

public class FieldValidator {

    /**
     * Used to get strings from resources
     */
    Context context;

    /**
     * Regex patern for valid emails
     */
    private static String emailRegex = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

    public FieldValidator(Context context) {
        this.context = context;
    }

    /**
     * Assert text isn't empty
     * @param text
     * @return
     */
    public boolean required(EditText text) {
        if (text.length() == 0) {
            text.setError(context.getString(R.string.form_required_field));
            return false;
        }

        return true;
    }

    /**
     * Assert password is long enough
     * @param text
     * @return
     */
    public boolean password(EditText text) {
        if (text.length() < 8) {
            text.setError(context.getString(R.string.form_invalid_password));
            return false;
        }

        return true;
    }

    /**
     * Assert email matches regex
     * @param text
     * @return
     */
    public boolean email(EditText text) {
        if (!text.getText().toString().matches(emailRegex)) {
            text.setError(context.getString(R.string.form_invalid_email));
            return false;
        }

        return true;
    }

    /**
     * Assert terms and conditions are accepted
     * @param checkBox
     * @return
     */
    public boolean termsAndConditions(CheckBox checkBox) {
        if (!checkBox.isChecked()) {
            checkBox.setError(context.getString(R.string.form_invalid_terms_and_conditions));
            return false;
        }

        return true;
    }

    /**
     * Assert credit card number is long enough
     * @param text
     * @return
     */
    public boolean creditCard(EditText text) {
        if (text.length() < 16) {
            text.setError(context.getString(R.string.form_invalid_credit_card));
            return false;
        }

        return true;
    }
}
