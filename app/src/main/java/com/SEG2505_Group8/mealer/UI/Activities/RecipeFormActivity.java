package com.SEG2505_Group8.mealer.UI.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.SEG2505_Group8.mealer.Database.Models.MealerRecipe;
import com.SEG2505_Group8.mealer.Database.Models.MealerRole;
import com.SEG2505_Group8.mealer.Database.Models.MealerUser;
import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;
import com.SEG2505_Group8.mealer.UI.Activities.Utils.FieldValidator;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Presents form for creating a recipe.
 */
public class RecipeFormActivity extends AppCompatActivity {

    FieldValidator validator;

    EditText name;
    EditText course;
    EditText category;
    EditText ingredient;
    EditText allergen;
    EditText price;
    EditText description;

    ChipGroup categories;
    ChipGroup ingredients;
    ChipGroup allergens;

    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_form);

        name = findViewById(R.id.recipe_form_name);
        course = findViewById(R.id.recipe_form_course);

        category = findViewById(R.id.recipe_form_categories);

        category.setOnEditorActionListener((v, actionId, event) -> {

            // If triggered by an enter key, this is the event; otherwise, this is null.
            if (event != null) {
                // if shift key is down, then we want to insert the '\n' char in the TextView;
                // otherwise, the default action is to send the message.
                if (!event.isShiftPressed()) {
                    if (!category.getText().toString().equals("")) {
                        Chip chip = new Chip(this);

                        chip.setOnClickListener(c -> {
                            categories.removeView(c);
                        });

                        chip.setText(category.getText().toString());
                        chip.setCloseIconVisible(true);

                        categories.addView(chip);
                        category.setText("");
                        category.setError(null);
                    }
                    return true;
                }

                if (!category.getText().toString().equals("")) {
                    Chip chip = new Chip(this);

                    chip.setOnClickListener(c -> {
                        categories.removeView(c);
                    });

                    chip.setText(category.getText().toString());
                    chip.setCloseIconVisible(true);

                    categories.addView(chip);
                    category.setText("");
                    category.setError(null);
                }

                return true;
            }

            return false;
        });

        ingredient = findViewById(R.id.recipe_form_ingredients);
        ingredient.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {

                if (!ingredient.getText().toString().equals("")) {
                    Chip chip = new Chip(this);

                    chip.setOnClickListener(c -> {
                        ingredients.removeView(c);
                    });

                    chip.setText(ingredient.getText().toString());
                    chip.setCloseIconVisible(true);

                    ingredients.addView(chip);
                    ingredient.setText("");
                    ingredient.setError(null);
                }

                return true;
            }
            return false;
        });

        allergen = findViewById(R.id.recipe_form_allergens);
        allergen.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {

                if (!allergen.getText().toString().equals("")) {
                    Chip chip = new Chip(this);

                    chip.setOnClickListener(c -> {
                        allergens.removeView(c);
                    });

                    chip.setText(allergen.getText().toString());
                    chip.setCloseIconVisible(true);

                    allergens.addView(chip);
                    allergen.setText("");
                    allergen.setError(null);
                }

                return true;
            }
            return false;
        });

        price = findViewById(R.id.recipe_form_price);
        description = findViewById(R.id.recipe_form_description);

        categories = findViewById(R.id.recipe_form_category_chips);
        ingredients = findViewById(R.id.recipe_form_ingredient_chips);
        allergens = findViewById(R.id.recipe_form_allergen_chips);

        submitButton = findViewById(R.id.recipe_form_submit_button);
        submitButton.setOnClickListener(view -> {

            // Only submit if all fields are valid
            if(validateFields()){
                // TODO: Implement submit function call
                submit(name.getText().toString(), course.getText().toString(), validator.aggregateChips(categories), validator.aggregateChips(ingredients), validator.aggregateChips(allergens), Float.parseFloat(price.getText().toString()), description.getText().toString(), false);
                finish();
            }
        });

        validator = new FieldValidator(getApplicationContext());
    }

    /**
     * Submit form info to create a Chef account
     */
    private void submit(String name, String course, List<String> categories, List<String> ingredients, List<String> allergens, float price, String description, boolean offered) {
        Services.getDatabaseClient().getUserMenu(menu -> {
            menu.addRecipe(new MealerRecipe(UUID.randomUUID().toString(), name, course, categories, ingredients, allergens, price, description, offered));
        });
    }

    /**
     * Assert all fields are valid
     * @return
     */
    private boolean validateFields() {
        // TODO: Price must be numeric
        return validator.required(name) && validator.required(course) && validator.required(price);
    }
}