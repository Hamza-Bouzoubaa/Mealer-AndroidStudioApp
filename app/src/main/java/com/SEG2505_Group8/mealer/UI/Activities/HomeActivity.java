package com.SEG2505_Group8.mealer.UI.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.SEG2505_Group8.mealer.Database.Models.MealerComplaint;
import com.SEG2505_Group8.mealer.Database.Models.MealerRole;
import com.SEG2505_Group8.mealer.Database.Models.MealerUser;
import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;
import com.SEG2505_Group8.mealer.UI.Adapters.ViewPager2Adapter;
import com.SEG2505_Group8.mealer.UI.Fragments.ComplaintListFragment;
import com.SEG2505_Group8.mealer.UI.Fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Welcome Activity for sign in users.
 * Presents current status and a logout button.
 */
public class HomeActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    BottomNavigationView bottomNavigationView;

    RecommendationsFragment recommendationsFragment;
    ComplaintListFragment complaintListFragment;
    SettingsFragment settingsFragment;

    List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Go back to MainActivity if user is no longer signed in.
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent i = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        viewPager = findViewById(R.id.home_view_pager);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                Fragment f = fragments.get(position);

                if (f.equals(recommendationsFragment)) {
                    bottomNavigationView.getMenu().findItem(R.id.bottom_navigation_menu_page_recommendations).setChecked(true);
                } else if (f.equals(complaintListFragment)) {
                    bottomNavigationView.getMenu().findItem(R.id.bottom_navigation_menu_page_complaints).setChecked(true);
                } else if (f.equals(settingsFragment)) {
                    bottomNavigationView.getMenu().findItem(R.id.bottom_navigation_menu_page_settings).setChecked(true);
                }
            }
        });

        bottomNavigationView = findViewById(R.id.home_bottom_navigation_view);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_navigation_menu_page_recommendations:
                    viewPager.setCurrentItem(fragments.indexOf(recommendationsFragment), false);
                    return true;
                case R.id.bottom_navigation_menu_page_complaints:
                    viewPager.setCurrentItem(fragments.indexOf(complaintListFragment), false);
                    return true;
                case R.id.bottom_navigation_menu_page_settings:
                    viewPager.setCurrentItem(fragments.indexOf(settingsFragment), false);
                    return true;
                default:
                    return false;
            }
        });

        setupViewPager();

        // Create some dummy complaints
        Services.getDatabaseClient().updateComplaint(new MealerComplaint("complaint1", "chef1", "user1", "A fancy description"));
        Services.getDatabaseClient().updateComplaint(new MealerComplaint("complaint2", "chef2", "user1", "A trash description"));
        Services.getDatabaseClient().updateComplaint(new MealerComplaint("complaint3", "chef3", "user1", "A hungry description"));
        Services.getDatabaseClient().updateComplaint(new MealerComplaint("complaint4", "chef4", "user1", "A fast description"));
        Services.getDatabaseClient().updateComplaint(new MealerComplaint("complaint5", "chef5", "user1", "A little description"));
    }

    /**
     * Setup view pager with desired fragments using {@link ViewPager2Adapter}
     */
    private void setupViewPager() {
        recommendationsFragment = new RecommendationsFragment();
        complaintListFragment = new ComplaintListFragment();
        settingsFragment = new SettingsFragment();

        fragments = new ArrayList<>();

        Services.getDatabaseClient().getUser(user -> {
            ViewPager2Adapter adapter = new ViewPager2Adapter(getSupportFragmentManager(), getLifecycle());

            fragments.add(recommendationsFragment);
            adapter.add(recommendationsFragment);

            if (user.getRole() == MealerRole.ADMIN) {
                fragments.add(complaintListFragment);
                adapter.add(complaintListFragment);
            }

            adapter.add(settingsFragment);
            fragments.add(settingsFragment);

            viewPager.setAdapter(adapter);

        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Listen for current user's name and role
        Services.getDatabaseClient().listenForModel(this, "users", FirebaseAuth.getInstance().getCurrentUser().getUid(), MealerUser.class, user -> {

            // Update preferences
            String role = "null";
            switch (user.getRole()){
                case CHEF:
                    role = getString(R.string.role_chef);
                    break;
                case USER:
                    role = getString(R.string.role_user);
                    break;
                case ADMIN:
                    role = getString(R.string.role_admin);
                    break;
            }

            role = role.substring(0,1).toUpperCase() + role.substring(1);

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);

            SharedPreferences.Editor editor = settings.edit();
            editor.putString("first_name", user.getFirstName());
            editor.putString("last_name", user.getLastName());
            editor.putString("address", user.getAddress());
            editor.putString("role", role);
            editor.apply();

            settingsFragment.refresh();

            // Update available menus
            bottomNavigationView.getMenu().findItem(R.id.bottom_navigation_menu_page_recommendations).setVisible(true);
            bottomNavigationView.getMenu().findItem(R.id.bottom_navigation_menu_page_complaints).setVisible(user.getRole() == MealerRole.ADMIN);
            bottomNavigationView.getMenu().findItem(R.id.bottom_navigation_menu_page_settings).setVisible(true);
        });
    }
}