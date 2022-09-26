package com.SEG2505_Group8.mealer.Database;

import com.SEG2505_Group8.mealer.Database.Models.MealerMenu;
import com.SEG2505_Group8.mealer.Database.Models.MealerRecipe;
import com.SEG2505_Group8.mealer.Database.Models.MealerUser;

public interface DatabaseClient {

    DatabaseClient instance = new FirebaseClient();

    /**
     * Get {@link MealerUser} with document Id.
     *
     * @param id
     * @return
     */
    MealerUser getUser(String id);

    /**
     * Get {@link MealerUser} document of User who is logged in.
     *
     * @return
     */
    MealerUser getUser();

    /**
     * Get {@link MealerMenu} of user with id.
     *
     * @param id
     * @return
     */
    MealerMenu getUserMenu(String id);

    /**
     * Get {@link MealerMenu} of logged in user.
     *
     * @return
     */
    MealerMenu getUserMenu();

    /**
     * Get {@link MealerMenu} with Document Id.
     *
     * @param id
     * @return
     */
    MealerMenu getMenu(String id);

    MealerRecipe getRecipe(String id);

    boolean updateRecipe(MealerRecipe recipe);

    boolean updateMenu(MealerMenu menu);

    boolean updateUser(MealerUser user);
}
