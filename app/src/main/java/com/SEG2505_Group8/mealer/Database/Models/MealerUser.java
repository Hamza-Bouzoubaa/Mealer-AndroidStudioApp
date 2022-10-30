package com.SEG2505_Group8.mealer.Database.Models;

import com.SEG2505_Group8.mealer.Database.Serialize.MealerSerializable;
import com.SEG2505_Group8.mealer.Database.Serialize.MealerSerializableElement;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a user.
 */
@Data
@IgnoreExtraProperties
@AllArgsConstructor
@NoArgsConstructor
public class MealerUser implements MealerSerializable {

    /**
     * Id in the database
     */
    String id;

    /**
     * User's first name
     */
    @MealerSerializableElement(key = "firstName")
    String firstName;

    /**
     * User's last name
     */
    @MealerSerializableElement(key = "lastName")
    String lastName;

    /**
     * User's email
     */
    @MealerSerializableElement(key = "email")
    String email;

    /**
     * User's address.
     * If role == USER then this is the default delivery address
     * If role == CHEF then this is the kitchen address
     */
    @MealerSerializableElement(key = "address")
    String address;

    /**
     * User's credit card number.
     * if role == USER, contains credit card number
     * else, empty string
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
    @MealerSerializableElement(key = "description")
    String description;

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
    List<Integer> ratings;

    /**
     * Total number of sales.
     * More granular stats can be queried at runtime.
     */
    @MealerSerializableElement(key = "totalSales")
    int totalSales;

    @MealerSerializableElement(key = "isSuspended")
    boolean isSuspended;

    @MealerSerializableElement(key = "suspendedUntil")
    String suspendedUntil;

    public MealerUser(String id, MealerRole role, String firstName, String lastName, String email, String address, String creditCard, String description, String voidCheckUrl) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.creditCard = creditCard;
        this.description = description;
        this.menuId = "";
        this.profilePictureUrl = "";
        this.voidCheckUrl = voidCheckUrl;
        this.role = role;
        this.totalSales = 0;
        this.isSuspended = false;
        this.suspendedUntil = "";

        // TODO: Make this cleaner
        List<Integer> ratings = new ArrayList<>();
        ratings.add(0);
        ratings.add(0);
        ratings.add(0);
        ratings.add(0);
        ratings.add(0);
        this.ratings = ratings;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
