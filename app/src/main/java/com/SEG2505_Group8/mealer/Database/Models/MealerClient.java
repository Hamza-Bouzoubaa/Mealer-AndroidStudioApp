package com.SEG2505_Group8.mealer.Database.Models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MealerClient extends MealerUser {

    public MealerClient(String id, String firstName, String lastName, String email, String address, String creditCard, String description) {
        super(id, MealerRole.USER, firstName, lastName, email, address, creditCard, description, "");
    }
}
