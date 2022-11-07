package com.SEG2505_Group8.mealer.UI.Activities;

import static java.lang.System.exit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.SEG2505_Group8.mealer.Database.Models.MealerComplaint;
import com.SEG2505_Group8.mealer.Database.Models.MealerMenu;
import com.SEG2505_Group8.mealer.Database.Models.MealerRecipe;
import com.SEG2505_Group8.mealer.Database.Models.MealerRole;
import com.SEG2505_Group8.mealer.Database.Models.MealerUser;
import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;
import com.SEG2505_Group8.mealer.UI.Adapters.ViewPager2Adapter;
import com.SEG2505_Group8.mealer.UI.Fragments.ComplaintListFragment;
import com.SEG2505_Group8.mealer.UI.Fragments.MenuFragment;
import com.SEG2505_Group8.mealer.UI.Fragments.RecommendationsFragment;
import com.SEG2505_Group8.mealer.UI.Fragments.SettingsFragment;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Welcome Activity for sign in users.
 * Presents current status and a logout button.
 */
public class HomeActivity extends AppCompatActivity {

    FloatingActionButton fab;

    ViewPager2 viewPager;
    BottomNavigationView bottomNavigationView;

    RecommendationsFragment recommendationsFragment;
    ComplaintListFragment complaintListFragment;
    SettingsFragment settingsFragment;
    MenuFragment menuFragment;

    List<Fragment> fragments;
    List<Integer> views;

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

        views = new ArrayList<>();
        views.add(R.id.bottom_navigation_menu_page_recommendations);
        views.add(R.id.bottom_navigation_menu_page_complaints);
        views.add(R.id.bottom_navigation_menu_page_settings);
        views.add(R.id.bottom_navigation_menu_page_menu);

        viewPager = findViewById(R.id.home_view_pager);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                Fragment f = fragments.get(position);

                int id;

                if (recommendationsFragment.equals(f)) {
                    id = R.id.bottom_navigation_menu_page_recommendations;
                } else if (complaintListFragment.equals(f)) {
                    id = R.id.bottom_navigation_menu_page_complaints;
                } else if (settingsFragment.equals(f)) {
                    id = R.id.bottom_navigation_menu_page_settings;
                } else if (menuFragment.equals(f)) {
                    id = R.id.bottom_navigation_menu_page_menu;
                } else {
                    throw new IndexOutOfBoundsException("Fragment not found in fragments list.");
                }

                bottomNavigationView.getMenu().findItem(id).setChecked(true);
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
                case R.id.bottom_navigation_menu_page_menu:
                    viewPager.setCurrentItem(fragments.indexOf(menuFragment), false);
                default:
                    return false;
            }
        });

        setupViewPager();

        List<String> categories = new ArrayList<>();
        categories.add("Italien");

        List<String> ingredients = new ArrayList<>();
        ingredients.add("crust");
        ingredients.add("sauce");

        List<String> allergens = new ArrayList<>();
        allergens.add("garlic");

        Services.getDatabaseClient().updateRecipe(new MealerRecipe("recipe1", "Pizza", "main", categories, ingredients, allergens, 10.0f, "a pizza recipe", true), object -> {});

        fab = findViewById(R.id.home_bottom_navigation_fab);

        List<String> recipes = new ArrayList<>();
        recipes.add("recipe1");

        List<Integer> ratings = new ArrayList<>();
        ratings.add(0);
        ratings.add(0);
        ratings.add(0);
        ratings.add(0);
        ratings.add(0);
        Services.getDatabaseClient().updateMenu(new MealerMenu("menu1", "m2nZ7KiHJyRbzvhkaGMkuB2JZ9M2", recipes, ratings), object -> {});

        Services.getDatabaseClient().getUser(user -> {
            user.setMenuId("menu1");
            Services.getDatabaseClient().updateUser(user);
        });
    }

    /**
     * Setup view pager with desired fragments using {@link ViewPager2Adapter}
     */
    private void setupViewPager() {
        recommendationsFragment = new RecommendationsFragment();
        complaintListFragment = new ComplaintListFragment();
        settingsFragment = new SettingsFragment();
        menuFragment = new MenuFragment();

        fragments = new ArrayList<>();

        Services.getDatabaseClient().getUser(user -> {
            ViewPager2Adapter adapter = new ViewPager2Adapter(getSupportFragmentManager(), getLifecycle());

            if (user == null) {
                AuthUI.getInstance().signOut(HomeActivity.this).addOnCompleteListener(view -> {
                    Intent i = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                });
                return;
            }

            if (user.getRole() == MealerRole.ADMIN) {

            } else {

            }

            switch (user.getRole()) {
                case ADMIN:
                    fragments.add(complaintListFragment);
                    adapter.add(complaintListFragment);
                    break;
                case USER:
                    fragments.add(recommendationsFragment);
                    adapter.add(recommendationsFragment);
                    break;
                case CHEF:
                    fragments.add(menuFragment);
                    adapter.add(menuFragment);
                    break;
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

            if (user == null) {
                return;
            }

            // Update preferences
            String role = "null";
            switch (user.getRole()){
                case CHEF:
                    role = getString(R.string.role_chef);
                    fab.setOnClickListener(view -> {
                        startActivity(new Intent(HomeActivity.this, RecipeFormActivity.class));
                    });
                    break;
                case USER:
                    role = getString(R.string.role_user);
                    break;
                case ADMIN:
                    role = getString(R.string.role_admin);
                    fab.setOnClickListener(view -> {
                        Services.getDatabaseClient().updateComplaint(new MealerComplaint(UUID.randomUUID().toString(), "m2nZ7KiHJyRbzvhkaGMkuB2JZ9M2","OzG6d9CjlMTuG8idUQ2ovAv70xn1", "Terrible food!"));
                        Services.getDatabaseClient().updateComplaint(new MealerComplaint(UUID.randomUUID().toString(), "m2nZ7KiHJyRbzvhkaGMkuB2JZ9M2","OzG6d9CjlMTuG8idUQ2ovAv70xn1", "Health hazard!"));
                        Services.getDatabaseClient().updateComplaint(new MealerComplaint(UUID.randomUUID().toString(), "m2nZ7KiHJyRbzvhkaGMkuB2JZ9M2","OzG6d9CjlMTuG8idUQ2ovAv70xn1", "0 stars!"));
                        Services.getDatabaseClient().updateComplaint(new MealerComplaint(UUID.randomUUID().toString(), "m2nZ7KiHJyRbzvhkaGMkuB2JZ9M2","OzG6d9CjlMTuG8idUQ2ovAv70xn1", "Garbage!"));
                        Services.getDatabaseClient().updateComplaint(new MealerComplaint(UUID.randomUUID().toString(), "m2nZ7KiHJyRbzvhkaGMkuB2JZ9M2","OzG6d9CjlMTuG8idUQ2ovAv70xn1", "Food 1 hour late!"));
                    });
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
            for (Integer id : views) {
                MenuItem item = bottomNavigationView.getMenu().findItem(id);
                if (item != null) {
                    item.setVisible(false);
                }
            }

            for (Integer id : user.getAvailableViews()) {
                MenuItem item = bottomNavigationView.getMenu().findItem(id);
                if (item != null) {
                    item.setVisible(true);
                }
            }

            // If user is suspended, show suspension alert
            if (user.isSuspended()) {
                showSuspensionAlert(user.getSuspendedUntil());
            }
        });
    }

    /**
     * Display alert stating suspension and its end date.
     * @param suspensionEndDate ISO 8601 formatted date time string
     */
    private void showSuspensionAlert(String suspensionEndDate)  {

        // Create AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Create alert message
        String message;
        if (suspensionEndDate != null && !suspensionEndDate.equals("")) {
            message = getString(R.string.alert_suspension_description);

            try {
                // Create UTC formatter
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");

                // Parse suspensionEndDate using UTC formatter
                Date date = format.parse(suspensionEndDate);

                // Replace %s in message with date.
                message = message.replace("%s", date.toString() );
            } catch (ParseException | NullPointerException e) {
                // Failed to parse, display as raw ISO 8601 string
                message = message.replace("%s", suspensionEndDate);
            }
        } else {
            // No suspensionEndDate supplied, assume suspension is permanent.
            message = getString(R.string.alert_suspension_permanent_description);
        }

        // Configure builder
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.alert_suspension_signout), (dialog, id) -> {
                    // Right most button clicked, sign out and go to MainActivity
                    AuthUI.getInstance().signOut(this).addOnSuccessListener(unused -> {
                        dialog.cancel();
                        Intent i = new Intent(HomeActivity.this, MainActivity.class);
                        startActivity(i);
                        Toast.makeText(getApplicationContext(),"User Sign Out.", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                })
                .setNegativeButton(getString(R.string.alert_suspension_quit), (dialog, id) -> {
                    // Left most button clicked, Quit app
                    Toast.makeText(getApplicationContext(),"Quit Mealer", Toast.LENGTH_SHORT).show();
                    exit(0);
                }).setTitle("Title here");

        // Create alert using builder
        AlertDialog alert = builder.create();
        alert.setTitle(getString(R.string.alert_suspension_title));

        // Display alert
        alert.show();
    }
}