package com.SEG2505_Group8.mealer.UI.Activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.SEG2505_Group8.mealer.Database.Models.MealerComplaint;
import com.SEG2505_Group8.mealer.Database.Models.MealerRecipe;
import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.UI.Fragments.DatePickerFragment;

import java.util.*;


public class RecipeActivity extends AppCompatActivity {

    MealerRecipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        recipe = (MealerRecipe) getIntent().getSerializableExtra("recipe");

        TextView name = findViewById(R.id.name);
        name.setText(recipe.getName());

        TextView course = findViewById(R.id.course);
        course.setText(recipe.getCourse());

        TextView categories = findViewById(R.id.categories);
        List<String> category=new ArrayList<String>();
        category = recipe.getCategories();
        String joined = TextUtils.join(", ", category);
        categories.setText(joined);

        TextView ingredients = findViewById(R.id.ingredients);
        List<String> ingredient=new ArrayList<String>();
        ingredient = recipe.getIngredients();
        String ing = TextUtils.join(", ", ingredient);
        ingredients.setText(ing);

        TextView allergens = findViewById(R.id.allergens);
        List<String> allerg=new ArrayList<String>();
        allerg = recipe.getAllergens();
        String allergy = TextUtils.join(", ", allerg);
        allergens.setText(allergy);


        TextView price = findViewById(R.id.price);
        String PriceWithSign = '$'+String.valueOf(recipe.getPrice());
        price.setText(PriceWithSign);


    }
}