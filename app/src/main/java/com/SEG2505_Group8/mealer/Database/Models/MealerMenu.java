package com.SEG2505_Group8.mealer.Database.Models;

import com.SEG2505_Group8.mealer.Database.Serialize.MealerSerializable;
import com.SEG2505_Group8.mealer.Database.Serialize.MealerSerializableElement;
import com.SEG2505_Group8.mealer.Services;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a {@link MealerUser}'s menu
 */
@Data
@IgnoreExtraProperties
@AllArgsConstructor
@NoArgsConstructor
public class MealerMenu implements MealerSerializable {

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

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public void addRecipe(MealerRecipe recipe) {

        if (recipe.getId() == null || recipe.getId().equals("")) {
            recipe.setId(UUID.randomUUID().toString());
        }

        Services.getDatabaseClient().updateRecipe(recipe, success -> {
            if (success) {
                recipeIds.add(recipe.getId());
                Services.getDatabaseClient().updateMenu(this);
            }
        });
    }
}
