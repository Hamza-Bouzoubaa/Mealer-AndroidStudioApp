package com.SEG2505_Group8.mealer.Database.Models;

import com.SEG2505_Group8.mealer.Database.Serialize.MealerSerializable;
import com.SEG2505_Group8.mealer.Database.Serialize.MealerSerializableElement;
import com.google.firebase.firestore.IgnoreExtraProperties;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@MealerSerializable
@IgnoreExtraProperties
@AllArgsConstructor
public class MealerUser {

    /**
     * Document Id of user.
     */
    String id;

    /**
     * First name of user.
     */
    @MealerSerializableElement(key = "firstName")
    String firstName;

    /**
     * Last name of user.
     */
    @MealerSerializableElement(key = "lastName")
    String lastName;

    /**
     * User email;
     */
    @MealerSerializableElement(key = "email")
    String email;

    /**
     * User address.
     * If role == USER then this is the delivery address
     * If role == CHEF then this is the kitchen address
     */
    @MealerSerializableElement(key = "address")
    String address;

    /**
     * Credit card number for USER, empty String otherwise
     */
    @MealerSerializableElement(key = "creditCard")
    String creditCard;

    /**
     * Url to the user's profile picture. Image is loaded at runtime.
     */
    @MealerSerializableElement(key = "profilePictureUrl")
    String profilePictureUrl;

    /**
     * Url to the user's void check if CHEF, empty String otherwise.
     */
    @MealerSerializableElement(key = "voidCheckUrl")
    String voidCheckUrl;

    /**
     * User description.
     */
    @MealerSerializableElement(key = "biography")
    String biography;

    /**
     * Id of menu if CHEF, empty String otherwise. Links to Document containing a CHEF's menu.
     */
    @MealerSerializableElement(key = "menuId")
    String menuId;

    /**
     * Type of user. {@link MealerRole}
     */
    @MealerSerializableElement(key = "role")
    MealerRole role;

    /**
     * Ratings of a user.
     * If CHEF, then USERs can review.
     * If USER, then CHEFs can review.
     */
    @MealerSerializableElement(key = "ratings")
    int[] ratings;

    /**
     * Total number of sales.
     * More granular stats can be queried at runtime.
     */
    @MealerSerializableElement(key = "totalSales")
    int totalSales;
}
