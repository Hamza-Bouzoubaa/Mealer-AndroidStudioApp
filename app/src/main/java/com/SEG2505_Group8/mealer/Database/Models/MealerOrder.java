package com.SEG2505_Group8.mealer.Database.Models;

import com.SEG2505_Group8.mealer.Database.Serialize.MealerSerializable;
import com.SEG2505_Group8.mealer.Database.Serialize.MealerSerializableElement;
import com.google.firebase.firestore.IgnoreExtraProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@MealerSerializable
@IgnoreExtraProperties
@AllArgsConstructor
@NoArgsConstructor

public class MealerOrder {
    @MealerSerializableElement(key = "status")
    MealerOrderStatus status;

    @MealerSerializableElement(key = "client")
    MealerUser client;

    @MealerSerializableElement(key = "recipe")
    MealerRecipe recipe;

    @MealerSerializableElement(key = "date")
    String date;
}
