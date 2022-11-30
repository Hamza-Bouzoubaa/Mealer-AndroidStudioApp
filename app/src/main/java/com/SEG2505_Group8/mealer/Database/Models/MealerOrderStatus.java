package com.SEG2505_Group8.mealer.Database.Models;

/**
 * Enum of order statuses
 */
public enum MealerOrderStatus {
    WAITING("WAITING"),
    ACCEPTED("ACCEPTED"),
    REJECTED("REJECTED"),
    COMPLETED("COMPLETED");

    private final String friendlyName;

    MealerOrderStatus(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @Override
    public String toString(){
        return friendlyName;
    }
}
