package com.SEG2505_Group8.mealer.Database.Models;

import com.SEG2505_Group8.mealer.Database.Serialize.MealerSerializable;
import com.SEG2505_Group8.mealer.Database.Serialize.MealerSerializableElement;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@MealerSerializable
@IgnoreExtraProperties
@AllArgsConstructor
@NoArgsConstructor
public class MealerRecipe {

    /**
     * Document id of Recipe.
     */
    String id;

    /**
     * Name of recipe.
     */
    @MealerSerializableElement(key = "name")
    String name;

    /**
     * Ex: Appetizer, Main, Dessert
     */
    @MealerSerializableElement(key = "course")
    String course;

    /**
     * Ex: Canadian, Thai, Chinese
     */
    @MealerSerializableElement(key = "categories")
    List<String> categories;

    /**
     * List of ingredients.
     */
    @MealerSerializableElement(key = "ingredients")
    List<String> ingredients;

    /**
     * List of allergens.
     */
    @MealerSerializableElement(key = "allergens")
    List<String> allergens;

    /**
     * Price to display. TODO: How does price localization work?
     */
    @MealerSerializableElement(key = "price")
    float price;

    /**
     * Contains a description and extra notes a chef would like to display.
     */
    @MealerSerializableElement(key = "description")
    String description;
}
