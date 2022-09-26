package com.SEG2505_Group8.mealer.Database.Models;

import com.google.firebase.firestore.IgnoreExtraProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@IgnoreExtraProperties
public class MealerRecipe {

    /**
     * Document id of Recipe.
     */
    String id;

    /**
     * Name of recipe.
     */
    String name;

    /**
     * Ex: Appetizer, Main, Dessert
     */
    String course;

    /**
     * Ex: Canadian, Thai, Chinese
     */
    String[] categories;

    /**
     * List of ingredients.
     */
    String[] ingredients;

    /**
     * List of allergens.
     */
    String[] allergens;

    /**
     * Price to display. TODO: How does price localization work?
     */
    float price;

    /**
     * Contains a description and extra notes a chef would like to display.
     */
    String description;
}
