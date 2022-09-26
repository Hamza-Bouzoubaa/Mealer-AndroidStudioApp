package com.SEG2505_Group8.mealer.Database.Models;

import com.SEG2505_Group8.mealer.Services;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@IgnoreExtraProperties
public class MealerMenu {

    /**
     * Document id of Menu
     */
    String id;

    /**
     * Document Id of chef who created and maintains this menu.
     */
    String chefId;

    /**
     * List of recipe ids that a chef cooks.
     */
    List<String> recipeIds;

    /**
     * Number of 1, 2, 3, 4, 5 star ratings.
     */
    int[] ratings;

    public List<MealerRecipe> getRecipes() {
        return recipeIds.stream().map(Services.getDatabaseClient()::getRecipe).collect(Collectors.toList());
    }
}
