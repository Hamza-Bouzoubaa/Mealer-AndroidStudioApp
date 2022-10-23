package com.SEG2505_Group8.mealer.UI.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.SEG2505_Group8.mealer.Database.Models.MealerUser;
import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;
import com.SEG2505_Group8.mealer.UI.Activities.MainActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

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

    @Override
    public void onResume() {
        super.onResume();

        if (FirebaseAuth.getInstance().getCurrentUser() == null || getActivity() == null) {
            return;
        }

//      Listen for current user's name and role
//        Services.getDatabaseClient().listenForModel(getActivity(), "users", FirebaseAuth.getInstance().getCurrentUser().getUid(), MealerUser.class, user -> {
//            if (getContext() == null) {
//                return;
//            }
//
//            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
//
//            SharedPreferences.Editor editor = settings.edit();
//            editor.putString("first_name", user.getFirstName());
//            editor.putString("last_name", user.getLastName());
//            editor.putString("address", user.getAddress());
//            editor.putString("role", user.getRole().toString());
//            editor.apply();
//        });
    }
}



