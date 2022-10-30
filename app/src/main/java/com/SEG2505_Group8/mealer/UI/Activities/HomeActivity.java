package com.SEG2505_Group8.mealer.UI.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.widget.ViewPager2;

import com.SEG2505_Group8.mealer.Database.Models.MealerComplaint;
import com.SEG2505_Group8.mealer.Database.Models.MealerUser;
import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;
import com.SEG2505_Group8.mealer.UI.Adapters.ViewPager2Adapter;
import com.SEG2505_Group8.mealer.UI.Fragments.ComplaintListFragment;
import com.SEG2505_Group8.mealer.UI.Fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Welcome Activity for sign in users.
 * Presents current status and a logout button.
 */
public class HomeActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    BottomNavigationView bottomNavigationView;

    ComplaintListFragment complaintListFragment;
    SettingsFragment settingsFragment;

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

                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.bottom_navigation_menu_page_complaints).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.bottom_navigation_menu_page_settings).setChecked(true);
                        break;
                }
            }
        });

        bottomNavigationView = findViewById(R.id.home_bottom_navigation_view);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_navigation_menu_page_complaints:
                    System.out.println("Clicked on page 1");
                    viewPager.setCurrentItem(0, false);
                    return true;
                case R.id.bottom_navigation_menu_page_settings:
                    System.out.println("Clicked on page 2");
                    viewPager.setCurrentItem(1, false);
                    return true;
                default:
                    return false;
            }
        });

        setupViewPager(viewPager);

        Services.getDatabaseClient().updateComplaint(new MealerComplaint("complaint1", "chef1", "user1", "A fancy description"));
        Services.getDatabaseClient().updateComplaint(new MealerComplaint("complaint2", "chef2", "user1", "A trash description"));
        Services.getDatabaseClient().updateComplaint(new MealerComplaint("complaint3", "chef3", "user1", "A hungry description"));
        Services.getDatabaseClient().updateComplaint(new MealerComplaint("complaint4", "chef4", "user1", "A fast description"));
        Services.getDatabaseClient().updateComplaint(new MealerComplaint("complaint5", "chef5", "user1", "A little description"));
    }

    /**
     * Setup view pager with desired fragments using {@link ViewPager2Adapter}
     * @param pager ViewPager2 to initialize
     */
    private void setupViewPager(ViewPager2 pager) {
        ViewPager2Adapter adapter = new ViewPager2Adapter(getSupportFragmentManager(), getLifecycle());

        complaintListFragment = new ComplaintListFragment();
        settingsFragment = new SettingsFragment();

        adapter.add(complaintListFragment);
        adapter.add(settingsFragment);

        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Listen for current user's name and role
        Services.getDatabaseClient().listenForModel(this, "users", FirebaseAuth.getInstance().getCurrentUser().getUid(), MealerUser.class, user -> {

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

            // TODO: Not currently listening properly
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);

            SharedPreferences.Editor editor = settings.edit();
            editor.putString("first_name", user.getFirstName());
            editor.putString("last_name", user.getLastName());
            editor.putString("address", user.getAddress());
            editor.putString("role", role);
            editor.apply();
        });
    }
}