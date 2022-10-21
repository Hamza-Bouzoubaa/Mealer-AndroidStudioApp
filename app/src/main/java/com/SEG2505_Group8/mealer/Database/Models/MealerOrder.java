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
@MealerSerializable
@IgnoreExtraProperties
@AllArgsConstructor
@NoArgsConstructor
public class MealerOrder {

    /**
     * Document id of Menu
     */
    String id;

    @MealerSerializableElement(key = "status")
    MealerOrderStatus status;

    /**
     * Id of user who created order
     */
    @MealerSerializableElement(key = "client")
    String clientId;

    /**
     * Id of recipe that was ordered
     */
    @MealerSerializableElement(key = "recipe")
    String recipeId;

    /**
     * Timestamp for order creation
     */
    @MealerSerializableElement(key = "timestamp")
    String timestamp;
}
