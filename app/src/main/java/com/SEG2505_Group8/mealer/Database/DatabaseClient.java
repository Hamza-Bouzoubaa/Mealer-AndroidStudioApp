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

    Task<Void> updateRecipe(MealerRecipe recipe);

    Task<Void> updateMenu(MealerMenu menu);

    Task<Void> updateUser(MealerUser user);

    boolean userDataInputRequired(MealerUser user);
}
