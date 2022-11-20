package com.SEG2505_Group8.mealer.UI.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;

/**
 * Presents form for creating a recipe.
 */
public class RecipeFormActivity extends AppCompatActivity {

    FieldValidator validator;

    Button submitButton;

    ImageView recipePreview;
    byte[] recipeBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_form);

        //recipePreview = findViewById(R.id.recipe_form_image);
        //Button recipeButton = findViewById(R.id.recipe_form_take_photo);

        // Launch camera on button press.
        //recipeButton.setOnClickListener(v -> {
        //    startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),100);
        //});

        //submitButton = findViewById(R.id.recipe_form_submit_button);
        submitButton.setOnClickListener(view -> {
            // Only submit if all fields are valid
            if(validateFields()){
                // TODO: Implement submit function call
//                submit();
            }
        });

        validator = new FieldValidator(getApplicationContext());
    }

    /**
     * Get data from camera activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap photo = (Bitmap)data.getExtras().get("data");

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
        recipeBytes = stream.toByteArray();

        recipePreview.setImageBitmap(photo);
    }

    /**
     * Submit form info to create a Chef account
     */
   // private void submit(String name, String course, List<String> categories, List<String> ingredients, List<String> allergens, float price, String description, boolean offered) {
   //     Services.getDatabaseClient().updateRecipe(new MealerRecipe(UUID.randomUUID().toString(), name, course, categories, ingredients, allergens, price, description, offered));
   // }

    /**
     * Assert all fields are valid
     * @return
     */
    private boolean validateFields() {
        return  recipeBytes != null;
    }
}