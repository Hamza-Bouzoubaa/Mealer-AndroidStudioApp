package com.SEG2505_Group8.mealer.Database.Models;

import com.SEG2505_Group8.mealer.Database.Serialize.MealerSerializable;
import com.SEG2505_Group8.mealer.Database.Serialize.MealerSerializableElement;
import com.google.firebase.firestore.IgnoreExtraProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an order
 */
@Data
@IgnoreExtraProperties
@AllArgsConstructor
@NoArgsConstructor
public class MealerOrder implements MealerSerializable {

    /**
     * Document id of Menu
     */
    String id;

    @MealerSerializableElement(key = "status")
    MealerOrderStatus status;

    @MealerSerializableElement(key = "chefId")
    String chefId;

    /**
     * Id of user who created order
     */
    @MealerSerializableElement(key = "clientId")
    String clientId;

    /**
     * Id of recipe that was ordered
     */
    @MealerSerializableElement(key = "recipeId")
    String recipeId;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public int getStatusStep() {
        return MealerOrderStatusUtils.toStep(status);
    }
}
