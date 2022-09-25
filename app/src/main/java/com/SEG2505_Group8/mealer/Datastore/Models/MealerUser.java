package com.SEG2505_Group8.mealer.Datastore.Models;

public class MealerUser {
    /**
     * First name of user.
     */
    String firstName;

    /**
     * Last name of user.
     */
    String lastName;

    /**
     * User email;
     */
    String email;

    /**
     * User address.
     * If role == USER then this is the delivery address
     * If role == CHEF then this is the kitchen address
     */
    String address;

    /**
     * Credit card number for USER, empty String otherwise
     */
    String creditCard;

    /**
     * Url to the user's profile picture. Image is loaded at runtime.
     */
    String profilePictureUrl;

    /**
     * Url to the user's void check if CHEF, empty String otherwise.
     */
    String voidCheckUrl;

    /**
     * User description.
     */
    String biography;

    /**
     * Id of menu if CHEF, empty String otherwise. Links to Document containing a CHEF's menu.
     */
    String menuId;

    /**
     * Type of user. {@link MealerRole}
     */
    MealerRole role;
}
