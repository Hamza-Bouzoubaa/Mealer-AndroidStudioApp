package com.SEG2505_Group8.mealer.Datastore.Models;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@IgnoreExtraProperties
public class Menu {
    /**
     * Document Id of chef who created and maintains this menu.
     */
    String chefId;

    /**
     * List of recipes{@link Recipe} that a chef cooks.
     */
    List<Recipe> recipes;

    /**
     * Number of 1, 2, 3, 4, 5 star ratings.
     */
    int[] ratings;
}
