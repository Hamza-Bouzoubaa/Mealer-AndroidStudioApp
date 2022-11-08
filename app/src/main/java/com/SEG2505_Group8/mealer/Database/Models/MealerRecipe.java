package com.SEG2505_Group8.mealer.Database.Models;

import com.SEG2505_Group8.mealer.Database.Serialize.MealerSerializable;
import com.SEG2505_Group8.mealer.Database.Serialize.MealerSerializableElement;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single recipe contained inside a {@link MealerMenu}
 */
@Data
@IgnoreExtraProperties
@AllArgsConstructor
@NoArgsConstructor
public class MealerRecipe implements MealerSerializable {

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

    @MealerSerializableElement(key = "isOffered")
    boolean isOffered;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
