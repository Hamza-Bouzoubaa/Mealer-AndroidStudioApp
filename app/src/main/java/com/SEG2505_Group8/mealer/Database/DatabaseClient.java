package com.SEG2505_Group8.mealer.Database;

import com.SEG2505_Group8.mealer.Database.Models.MealerMenu;
import com.SEG2505_Group8.mealer.Database.Models.MealerRecipe;
import com.SEG2505_Group8.mealer.Database.Models.MealerUser;

import java.util.concurrent.Future;

public interface DatabaseClient {

    /**
     * Get {@link MealerUser} with document Id.
     *
     * @param id
     * @return
     */
    Future<MealerUser> getUser(String id);

    /**
     * Get {@link MealerUser} document of User who is logged in.
     *
     * @return
     */
    Future<MealerUser> getUser();

    /**
     * Get {@link MealerMenu} of user with id.
     *
     * @param id
     * @return
     */
    Future<MealerMenu> getUserMenu(String id);

    /**
     * Get {@link MealerMenu} of logged in user.
     *
     * @return
     */
    Future<MealerMenu> getUserMenu();

    /**
     * Get {@link MealerMenu} with Document Id.
     *
     * @param id
     * @return
     */
    Future<MealerMenu> getMenu(String id);

    Future<MealerRecipe> getRecipe(String id);

    Future<Void> updateRecipe(MealerRecipe recipe);

    Future<Void> updateMenu(MealerMenu menu);

    Future<Void> updateUser(MealerUser user);

    Future<Boolean> userInfoRequired();
}
