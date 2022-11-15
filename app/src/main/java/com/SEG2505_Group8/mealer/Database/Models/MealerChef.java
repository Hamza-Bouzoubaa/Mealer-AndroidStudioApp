package com.SEG2505_Group8.mealer.Database.Models;

public class MealerChef extends MealerUser {

    public MealerChef(String id, String firstName, String lastName, String email, String address, String description, String voidCheckUrl) {
        super(id, MealerRole.CHEF, firstName, lastName, email, address, "", description, voidCheckUrl);
    }

}
