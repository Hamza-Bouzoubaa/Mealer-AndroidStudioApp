package com.SEG2505_Group8.mealer.Database.Models;

import com.SEG2505_Group8.mealer.Database.Serialize.MealerSerializable;
import com.SEG2505_Group8.mealer.Database.Serialize.MealerSerializableElement;
import com.SEG2505_Group8.mealer.Database.Utils.RatingUtils;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    Float price;

    /**
     * Contains a description and extra notes a chef would like to display.
     */
    @MealerSerializableElement(key = "description")
    String description;

    @MealerSerializableElement(key = "isOffered")
    Boolean isOffered;

    @MealerSerializableElement(key = "chefId")
    String chefId;

    /**
     * Number of 1, 2, 3, 4, 5 star ratings.
     */
    @MealerSerializableElement(key = "ratings")
    Map<String, List<String>> ratings;

    public MealerRecipe(String id, String name, String course, List<String> categories, List<String> ingredients, List<String> allergens, float price, String description, boolean isOffered, String chefId) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.categories = categories;
        this.ingredients = ingredients;
        this.allergens = allergens;
        this.price = price;
        this.description = description;
        this.isOffered = isOffered;
        this.chefId = chefId;
        this.ratings = new HashMap<>();
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public void rate(int rating, String userId) {
        if (ratings == null) {
            ratings = new HashMap<>();
        }
        RatingUtils.rate(ratings, rating, userId);
    }

    public float averageRating() {
        return RatingUtils.averageRating(ratings);
    }
}
