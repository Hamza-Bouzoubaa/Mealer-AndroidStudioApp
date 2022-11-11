package com.SEG2505_Group8.mealer.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.SEG2505_Group8.mealer.Database.Models.MealerComplaint;
import com.SEG2505_Group8.mealer.Database.Models.MealerRecipe;
import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;
import com.SEG2505_Group8.mealer.UI.Fragments.DatePickerFragment;

import java.util.*;


public class RecipeActivity extends AppCompatActivity {

    MealerRecipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        recipe = (MealerRecipe) getIntent().getSerializableExtra("recipe");

        TextView name = findViewById(R.id.recipe_title);
        name.setText(recipe.getName());

        TextView course = findViewById(R.id.recipe_course);
        course.setText(recipe.getCourse());

        TextView categories = findViewById(R.id.recipe_categories);
        categories.setText(TextUtils.join(", ", recipe.getCategories()));

        TextView ingredients = findViewById(R.id.recipe_ingredients);
        ingredients.setText(TextUtils.join(", ", recipe.getIngredients()));

        TextView allergens = findViewById(R.id.recipe_allergens);
        allergens.setText(TextUtils.join(", ", recipe.getAllergens()));

        TextView price = findViewById(R.id.recipe_price);
        price.setText('$' + String.valueOf(recipe.getPrice()));

        CheckBox isOffered = findViewById(R.id.recipe_offered);
        isOffered.setChecked(recipe.isOffered());

        Button deleteButton = findViewById(R.id.recipe_delete);
        deleteButton.setOnClickListener(view -> {
            if (isOffered.isChecked()) {
                isOffered.setError("Cannot delete recipe if currently offered!");
            } else {
                isOffered.setError(null);

                Services.getDatabaseClient().getUserMenu(menu -> {
                   menu.getRecipeIds().remove(recipe.getId());
                   Services.getDatabaseClient().updateMenu(menu);
                   Services.getDatabaseClient().deleteRecipe(recipe.getId(), success -> {
                       if (success) {
                           finish();
                       }
                   });
                });
            }
        });
    }
}