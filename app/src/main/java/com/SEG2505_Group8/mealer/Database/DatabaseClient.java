package com.SEG2505_Group8.mealer.Database;

import android.app.Activity;

import com.SEG2505_Group8.mealer.Database.Callbacks.DatabaseFilterCallback;
import com.SEG2505_Group8.mealer.Database.Callbacks.DatabaseCompletionCallback;
import com.SEG2505_Group8.mealer.Database.Callbacks.DatabaseSetCallback;
import com.SEG2505_Group8.mealer.Database.Models.MealerMenu;
import com.SEG2505_Group8.mealer.Database.Models.MealerRecipe;
import com.SEG2505_Group8.mealer.Database.Models.MealerUser;

import java.util.List;
import java.util.concurrent.Future;

public interface DatabaseClient {

    /**
     * Get {@link MealerUser} with document Id.
     *
     * @param id
     * @return
     */
    default Future<MealerUser> getUser(String id) {
        return getUser(id, null);
    }

    /**
     * Get {@link MealerUser} with document Id.
     * Calls callback on completion
     *
     * @param id
     * @param callback
     * @return
     */
    Future<MealerUser> getUser(String id, DatabaseCompletionCallback<MealerUser> callback);

    /**
     * Get {@link MealerUser} document of User who is logged in.
     *
     * @return
     */
    default Future<MealerUser> getUser() {
        return getUser(null, null);
    }

    /**
     * Get {@link MealerUser} document of User who is logged in.
     *
     * @return
     */
    default Future<MealerUser> getUser(DatabaseCompletionCallback<MealerUser> callback) {
        return getUser(null, callback);
    }

    /**
     * Get {@link MealerUser}s with filter
     */
    Future<List<MealerUser>> getUsers(DatabaseFilterCallback filter);

    /**
     * Get {@link MealerMenu} of user with id.
     *
     * @param id
     * @return
     */
    default Future<MealerMenu> getUserMenu(String id) {
        return getUserMenu(id, null);
    }

    /**
     * Get {@link MealerMenu} of user with id.
     *
     * @param id
     * @return
     */
    Future<MealerMenu> getUserMenu(String id, DatabaseCompletionCallback<MealerMenu> callback);

    /**
     * Get {@link MealerMenu} of logged in user.
     *
     * @return
     */
    default Future<MealerMenu> getUserMenu() {
        return getUserMenu(null, null);
    }

    /**
     * Get {@link MealerMenu} of logged in user.
     *
     * @return
     */
    default Future<MealerMenu> getUserMenu(DatabaseCompletionCallback<MealerMenu> callback) {
        return getUserMenu(null, callback);
    }

    /**
     * Get {@link MealerMenu}s with filter
     */
    Future<List<MealerMenu>> getMenus(DatabaseFilterCallback filter);

    /**
     * Get {@link MealerMenu} with Document Id.
     *
     * @param id
     * @return
     */
    default Future<MealerMenu> getMenu(String id) {
        return getMenu(id, null);
    }

    /**
     * Get {@link MealerMenu} with Document Id.
     *
     * @param id
     * @return
     */
    Future<MealerMenu> getMenu(String id, DatabaseCompletionCallback<MealerMenu> callback);

    /**
     * Get {@link MealerRecipe}s with filter
     */
    Future<List<MealerRecipe>> getRecipes(DatabaseFilterCallback filter);

    default Future<MealerRecipe> getRecipe(String id) {
        return getRecipe(id, null);
    }

    Future<MealerRecipe> getRecipe(String id, DatabaseCompletionCallback<MealerRecipe> callback);

    default Future<Boolean> updateRecipe(MealerRecipe recipe) {
        return updateRecipe(recipe, null);
    }

    Future<Boolean> updateRecipe(MealerRecipe recipe, DatabaseSetCallback callback);

    default Future<Boolean> updateMenu(MealerMenu menu) {
        return updateMenu(menu, null);
    }

    Future<Boolean> updateMenu(MealerMenu menu, DatabaseSetCallback callback);

    default Future<Boolean> updateUser(MealerUser user) {
        return updateUser(user, null);
    }

    Future<Boolean> updateUser(MealerUser user, DatabaseSetCallback callback);

    default Future<Boolean> deleteRecipe(String id) {
        return deleteRecipe(id, null);
    }

    Future<Boolean> deleteRecipe(String id, DatabaseCompletionCallback<MealerRecipe> callback);

    default Future<Boolean> userInfoRequired() {
        return userInfoRequired(null);
    }

    Future<Boolean> userInfoRequired(DatabaseCompletionCallback<Boolean> callback);

    <T> void listenForModel(Activity activity, String collectionId, String documentId, Class<T> clazz, DatabaseCompletionCallback<T> callback);
}
