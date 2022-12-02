package com.SEG2505_Group8.mealer.Database;

import android.app.Activity;
import android.content.Context;

import com.SEG2505_Group8.mealer.Database.Models.MealerOrder;
import com.SEG2505_Group8.mealer.Database.Utils.DatabaseFilterCallback;
import com.SEG2505_Group8.mealer.Database.Utils.DatabaseCompletionCallback;
import com.SEG2505_Group8.mealer.Database.Utils.DatabaseListener;
import com.SEG2505_Group8.mealer.Database.Utils.DatabaseSetCallback;
import com.SEG2505_Group8.mealer.Database.Models.MealerComplaint;
import com.SEG2505_Group8.mealer.Database.Models.MealerMenu;
import com.SEG2505_Group8.mealer.Database.Models.MealerRecipe;
import com.SEG2505_Group8.mealer.Database.Models.MealerUser;
import com.SEG2505_Group8.mealer.Database.Serialize.MealerSerializable;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Interface for accessing the database
 */
public interface DatabaseClient {

    /**
     * Get {@link MealerUser} with document Id.
     *
     * @param id
     * @return
     */
    default Future<MealerUser> getUser(String id) {
        return getUser(id, MealerUser.class, null);
    }

    /**
     * Get {@link MealerUser} with document Id.
     * Calls callback on completion
     *
     * @param id
     * @param callback
     * @return
     */
    default Future<MealerUser> getUser(String id, DatabaseCompletionCallback<MealerUser> callback) {
        return getUser(id, MealerUser.class, callback);
    }

    /**
     * Get {@link MealerUser} with document Id.
     * Cast to clazz
     * Calls callback on completion
     *
     * @param id
     * @param clazz
     * @param callback
     * @return
     */
    <T extends MealerUser> Future<T> getUser(String id, Class<T> clazz, DatabaseCompletionCallback<T> callback);

    /**
     * Get {@link MealerUser} document of User who is logged in.
     *
     * @return
     */
    default Future<MealerUser> getUser() {
        return getUser(null, MealerUser.class, null);
    }

    /**
     * Get {@link MealerUser} document of User who is logged in.
     * Calls callback on completion
     *
     * @return
     */
    default Future<MealerUser> getUser(DatabaseCompletionCallback<MealerUser> callback) {
        return getUser(null, MealerUser.class, callback);
    }

    /**
     * Get {@link MealerMenu} of user with id.
     * Calls callback on completion
     *
     * @param id
     * @param callback
     * @return
     */
    Future<MealerMenu> getUserMenu(String id, DatabaseCompletionCallback<MealerMenu> callback);

    /**
     * Get {@link MealerMenu} of logged in user.
     * Calls callback on completion
     *
     * @return
     */
    default Future<MealerMenu> getUserMenu(DatabaseCompletionCallback<MealerMenu> callback) {
        return getUserMenu(null, callback);
    }

    /**
     * Get {@link MealerOrder} with Document Id.
     *
     * @param id
     * @return
     */
    Future<MealerOrder> getOrder(String id, DatabaseCompletionCallback<MealerOrder> callback);

    /**
     * Get {@link MealerMenu} with Document Id.
     * Calls callback on completion
     *
     * @param id
     * @return
     */
    Future<MealerMenu> getMenu(String id, DatabaseCompletionCallback<MealerMenu> callback);


    /**
     * Get a {@link MealerRecipe} with id.
     * Calls callback on completion
     * @param id
     * @param callback
     * @return
     */
    Future<MealerRecipe> getRecipe(String id, DatabaseCompletionCallback<MealerRecipe> callback);

    /**
     * Search database for recipes matching filter
     * @param name to search for as substring
     * @param limit maximum number of recipes to return
     * @param callback to execute on search completion
     * @return
     */
    default Future<List<MealerRecipe>> searchRecipesByName(String name, int limit, DatabaseCompletionCallback<List<MealerRecipe>> callback) {
        return searchRecipes(limit, reference -> reference.whereEqualTo("isOffered", true).whereGreaterThanOrEqualTo("name", name).whereLessThanOrEqualTo("name", name + '\uF7FF'), callback);
    }

    Future<List<MealerRecipe>> searchRecipes(int limit, DatabaseFilterCallback filter, DatabaseCompletionCallback<List<MealerRecipe>> callback);

    /**
     * Update a {@link MealerRecipe} stored in Database.
     * Executes callback on completion.
     * @param recipe new version of recipe
     * @param callback code to execute on completion
     * @return
     */
    Future<Boolean> updateRecipe(MealerRecipe recipe, DatabaseSetCallback callback);

    /**
     * Update a complaint in the database. Executes callback on completion.
     * @param complaint
     * @param callback
     * @return
     */
    Future<Boolean> updateComplaint(MealerComplaint complaint, DatabaseSetCallback callback);

    /**
     * Update a complaint in the database.
     * @param complaint
     * @return
     */
    default Future<Boolean> updateComplaint(MealerComplaint complaint) {
        return updateComplaint(complaint, null);
    }

    /**
     * Update a {@link MealerMenu} stored in Database.
     *
     * @param menu
     * @return
     */
    default Future<Boolean> updateMenu(MealerMenu menu) {
        return updateMenu(menu, null);
    }

    /**
     * Update a {@link MealerMenu} stored in Database.
     * Executes callback on completion.
     *
     * @param menu
     * @param callback code to execute on completion
     * @return
     */
    Future<Boolean> updateMenu(MealerMenu menu, DatabaseSetCallback callback);

    /**
     * Update a {@link MealerUser} stored in Database.
     *
     * @param user
     * @return
     */
    default Future<Boolean> updateUser(MealerUser user) {
        return updateUser(user, null);
    }

    /**
     * Update a {@link MealerUser} stored in Database.
     * Executes callback on completion.
     *
     * @param user
     * @param callback code to execute on completion
     * @return
     */
    Future<Boolean> updateUser(MealerUser user, DatabaseSetCallback callback);

    /**
     * Update a {@link MealerOrder} stored in Database.
     * Executes callback on completion.
     *
     * @param order to place into database
     * @param callback code to execute on completion
     * @return
     */
    Future<Boolean> updateOrder(MealerOrder order, DatabaseSetCallback callback);

    /**
     * Create a {@link MealerOrder} for a specific {@link MealerRecipe}
     * @param menuId
     * @param recipeId
     * @param callback
     * @return
     */
    Future<MealerOrder> orderRecipe(MealerRecipe recipe, DatabaseCompletionCallback<MealerOrder> callback);

    /**
     * Delete a {@link MealerRecipe} stored in Database.
     * Executes callback on completion.
     *
     * @param id
     * @param callback
     * @return
     */
    Future<Boolean> deleteRecipe(String id, DatabaseSetCallback callback);

    /**
     * Delete a {@link MealerOrder} stored in Database.
     * Executes callback on completion.
     *
     * @param id
     * @param callback
     * @return
     */
    Future<Boolean> deleteOrder(String id, DatabaseSetCallback callback);

    /**
     * Delete a {@link MealerComplaint} stored in Database.
     * Executes callback on completion.
     *
     * @param id
     * @param callback
     * @return
     */
    Future<Boolean> deleteComplaint(String id, DatabaseSetCallback callback);

    Future<Boolean> updateUserToken();

    /**
     * Set status of a {@link MealerOrder} to REJECTED
     * Executes callback on completion
     * @param order
     * @param callback
     * @return
     */
    Future<Boolean> rejectOrder(MealerOrder order, Context context, DatabaseSetCallback callback);

    /**
     * Listen for a user's recipes.
     * Only show offered recipes if offeredRecipesOnly is true
     * @param activity
     * @param userId
     * @param offeredRecipesOnly
     * @param callback
     * @return
     */
    DatabaseListener listenForUserRecipes(Activity activity, String userId, boolean offeredRecipesOnly, DatabaseCompletionCallback<List<MealerRecipe>> callback);

    /**
     * Executes callback when document changes in database.
     *
     * @param activity
     * @param collectionId
     * @param documentId
     * @param clazz
     * @param callback
     * @return
     */
    <T extends MealerSerializable> DatabaseListener listenForModel(Activity activity, String collectionId, String documentId, Class<T> clazz, DatabaseCompletionCallback<T> callback);

    /**
     * Executes callback when collection changes in database.
     * Filters collection using filter.
     *
     * @param activity
     * @param collectionId
     * @param clazz
     * @param filter to get part of a collection
     * @param callback to execute when collection changes
     * @return
     */
    <T extends MealerSerializable> DatabaseListener listenForModels(Activity activity, String collectionId, Class<T> clazz, DatabaseFilterCallback filter, DatabaseCompletionCallback<List<T>> callback);

    /**
     * Executes callback when collection changes in database.
     *
     * @param activity
     * @param collectionId
     * @param clazz
     * @param callback to execute when collection changes
     * @return
     */
    default <T extends MealerSerializable> DatabaseListener listenForModels(Activity activity, String collectionId, Class<T> clazz, DatabaseCompletionCallback<List<T>> callback){
        return listenForModels(activity, collectionId, clazz, object -> object.limit(100), callback);
    }
}
