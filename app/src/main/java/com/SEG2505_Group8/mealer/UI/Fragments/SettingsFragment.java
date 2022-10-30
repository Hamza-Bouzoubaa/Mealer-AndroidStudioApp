package com.SEG2505_Group8.mealer.UI.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import com.SEG2505_Group8.mealer.Database.Models.MealerUser;
import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;
import com.SEG2505_Group8.mealer.UI.Activities.MainActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends PreferenceFragmentCompat {

    PreferenceScreen preferenceScreen;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        preferenceScreen = getPreferenceScreen();

        try {
            Preference signOutButton = findPreference("sign_out");
            signOutButton.setOnPreferenceClickListener(preference -> {
                // Call Firebase Authentication sign out

                AuthUI.getInstance().signOut(getContext()).addOnCompleteListener(task -> {
                    // Tell user they were signed out
                    Toast.makeText(getContext(), "User Signed Out", Toast.LENGTH_SHORT).show();

                    // Go to Main Activity
                    Intent i = new Intent(getContext(), MainActivity.class);
                    startActivity(i);

                    System.out.println("Activity: " + getActivity().toString());

                    getActivity().finish();
                });

                return true;
            });
        } catch (NullPointerException e) {
            Toast.makeText(getContext(), "Could not find preference: sign out!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Notify preferences to refresh themselves.
     */
    public void refresh() {

        // Only refresh if preferences already exist
        if (preferenceScreen != null) {
            // Clear existing prefs
            setPreferenceScreen(null);

            // Create new prefs
            addPreferencesFromResource(R.xml.root_preferences);

            // Update ref
            preferenceScreen = getPreferenceScreen();
        }
    }
}



