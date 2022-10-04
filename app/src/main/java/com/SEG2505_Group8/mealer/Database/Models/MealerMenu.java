package com.SEG2505_Group8.mealer.Database.Models;

import com.SEG2505_Group8.mealer.Database.Serialize.MealerSerializable;
import com.SEG2505_Group8.mealer.Database.Serialize.MealerSerializableElement;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@MealerSerializable
@IgnoreExtraProperties
@AllArgsConstructor
public class MealerMenu {

    /**
     * Document id of Menu
     */
    String id;

    /**
     * Document Id of chef who created and maintains this menu.
     */
    @MealerSerializableElement(key = "chefId")
    String chefId;

    /**
     * List of recipe ids that a chef cooks.
     */
    @MealerSerializableElement(key = "recipeIds")
    List<String> recipeIds;

    /**
     * Number of 1, 2, 3, 4, 5 star ratings.
     */
    @MealerSerializableElement(key = "ratings")
    List<Integer> ratings;
}
