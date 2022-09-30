package com.SEG2505_Group8.mealer.Database;

import com.SEG2505_Group8.mealer.Database.Models.MealerMenu;
import com.SEG2505_Group8.mealer.Database.Models.MealerRecipe;
import com.SEG2505_Group8.mealer.Database.Models.MealerUser;
import com.google.android.gms.tasks.Task;

public interface DatabaseClient {

    /**
     * Get {@link MealerUser} with document Id.
     *
     * @param id
     * @return
     */
    Task<MealerUser> getUser(String id);

    /**
     * Get {@link MealerUser} document of User who is logged in.
     *
     * @return
     */
    Task<MealerUser> getUser();

    /**
     * Get {@link MealerMenu} of user with id.
     *
     * @param id
     * @return
     */
    Task<MealerMenu> getUserMenu(String id);

    /**
     * Get {@link MealerMenu} of logged in user.
     *
     * @return
     */
    Task<MealerMenu> getUserMenu();

    /**
     * Get {@link MealerMenu} with Document Id.
     *
     * @param id
     * @return
     */
    Task<MealerMenu> getMenu(String id);

    Task<MealerRecipe> getRecipe(String id);

    Task<Void> updateRecipe(MealerRecipe recipe);

    Task<Void> updateMenu(MealerMenu menu);

    Task<Void> updateUser(MealerUser user);

    boolean userDataInputRequired(MealerUser user);
}
