package com.SEG2505_Group8.mealer.Database;

import com.SEG2505_Group8.mealer.Database.Models.MealerMenu;
import com.SEG2505_Group8.mealer.Database.Models.MealerRecipe;
import com.SEG2505_Group8.mealer.Database.Models.MealerUser;

public class FirebaseClient implements DatabaseClient {
    
    @Override
    public MealerUser getUser(String id) {
        return null;
    }

    @Override
    public MealerUser getUser() {
        return null;
    }

    @Override
    public MealerMenu getUserMenu(String id) {
        return null;
    }

    @Override
    public MealerMenu getUserMenu() {
        return null;
    }

    @Override
    public MealerMenu getMenu(String id) {
        return null;
    }

    @Override
    public MealerRecipe getRecipe(String id) {
        return null;
    }

    @Override
    public boolean updateRecipe(MealerRecipe recipe) {
        return false;
    }

    @Override
    public boolean updateMenu(MealerMenu menu) {
        return false;
    }

    @Override
    public boolean updateUser(MealerUser user) {
        return false;
    }
}