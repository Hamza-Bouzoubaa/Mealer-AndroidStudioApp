package com.SEG2505_Group8.mealer.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.SEG2505_Group8.mealer.Database.Models.MealerRecipe;
import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.Services;


public class RecipeClientActivity extends AppCompatActivity {

    MealerRecipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_client);

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

        Button closeButton = findViewById(R.id.recipe_close);
        closeButton.setOnClickListener(view -> {
            finish();
        });

        Button orderButton = findViewById(R.id.recipe_order);
        orderButton.setOnClickListener(view -> {
            if (recipe.getIsOffered()) {

                Services.getDatabaseClient().orderRecipe(recipe, order -> {
                    if (order != null) {
//                        Intent i = new Intent(RecipeClientActivity.this, OrderAc);
//                        startActivity();
                        System.out.println("Launching activity");
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "This recipe is not currently offered!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}