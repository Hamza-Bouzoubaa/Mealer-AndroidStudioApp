package com.SEG2505_Group8.mealer.Database.Models;

import com.SEG2505_Group8.mealer.Database.DatabaseClient;
import com.SEG2505_Group8.mealer.Database.Serialize.MealerSerializable;
import com.SEG2505_Group8.mealer.Database.Serialize.MealerSerializableElement;
import com.SEG2505_Group8.mealer.R;
import com.SEG2505_Group8.mealer.UI.Activities.Utils.DateUtils;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a user.
 */
@Data
@AllArgsConstructor
@IgnoreExtraProperties
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
    Integer totalSales;

    @MealerSerializableElement(key = "isSuspended")
    Boolean isSuspended;

    @MealerSerializableElement(key = "suspendedUntil")
    String suspendedUntil;

    @MealerSerializableElement(key = "orders")
    List<String> orderIds;

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
        this.orderIds = new ArrayList<>();

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

    public void suspend(Date suspensionEndDate) {
        isSuspended = true;
        if (suspensionEndDate != null) {
            this.suspendedUntil = DateUtils.toString(suspensionEndDate);
        } else {
            this.suspendedUntil = null;
        }
    }

    /**
     * Check if a user is suspended. Updates database if suspension end date has passed.
     * @return
     */
    public boolean isSuspended(DatabaseClient client) {

        if (isSuspended == null) {
            isSuspended = false;
        }

        if (suspendedUntil != null && !suspendedUntil.equals("") && DateUtils.isPassed(suspendedUntil)) {
            // Suspended, but date already passed. Disable suspension.
            isSuspended = false;
            suspendedUntil = null;

            client.updateUser(this);
        } else if (!isSuspended && suspendedUntil != null && !suspendedUntil.equals("")) {
            // Clear passed suspension date if not suspended.
            suspendedUntil = null;
            client.updateUser(this);
        }

        return isSuspended;
    }

    private static HashMap<MealerRole, List<Integer>> availableViews = new HashMap<MealerRole, List<Integer>>() {{
        List<Integer> adminViews = new ArrayList<>();
        adminViews.add(R.id.bottom_navigation_menu_page_recommendations);
        adminViews.add(R.id.bottom_navigation_menu_page_complaints);
        adminViews.add(R.id.bottom_navigation_menu_page_settings);

        put(MealerRole.ADMIN, adminViews);

        List<Integer> userViews = new ArrayList<>();
        userViews.add(R.id.bottom_navigation_menu_page_recommendations);
        userViews.add(R.id.bottom_navigation_menu_page_order);
        userViews.add(R.id.bottom_navigation_menu_page_settings);

        put(MealerRole.USER, userViews);

        List<Integer> chefViews = new ArrayList<>();
        chefViews.add(R.id.bottom_navigation_menu_page_recommendations);
        chefViews.add(R.id.bottom_navigation_menu_page_menu);
        chefViews.add(R.id.bottom_navigation_menu_page_order);
        chefViews.add(R.id.bottom_navigation_menu_page_settings);

        put(MealerRole.CHEF, chefViews);
    }};

    public List<Integer> getAvailableViews() {
        return availableViews.get(role);
    }
}
