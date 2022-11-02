package com.SEG2505_Group8.mealer.Database;

import android.app.Activity;

import com.SEG2505_Group8.mealer.Database.Callbacks.DatabaseFilterCallback;
import com.SEG2505_Group8.mealer.Database.Callbacks.DatabaseCompletionCallback;
import com.SEG2505_Group8.mealer.Database.Callbacks.DatabaseSetCallback;
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
     * Calls callback on completion
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
     * Calls callback on completion
     *
     * @param id
     * @param callback
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
     * Calls callback on completion
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
     * Calls callback on completion
     *
     * @param id
     * @return
     */
    Future<MealerMenu> getMenu(String id, DatabaseCompletionCallback<MealerMenu> callback);

    /**
     * Get {@link MealerRecipe}s with filter
     */
    Future<List<MealerRecipe>> getRecipes(DatabaseFilterCallback filter);

    /**
     * Get {@link MealerRecipe} with document Id.
     *
     * @param id
     * @return
     */
    default Future<MealerRecipe> getRecipe(String id) {
        return getRecipe(id, null);
    }

    /**
     * Get a {@link MealerRecipe} with id.
     * Calls callback on completion
     * @param id
     * @param callback
     * @return
     */
    Future<MealerRecipe> getRecipe(String id, DatabaseCompletionCallback<MealerRecipe> callback);

    /**
     * Page trough {@link MealerComplaint}.
     * Calls callback on completion
     * @param callback
     * @return
     */
    Future<List<MealerComplaint>> getComplaints(DatabaseFilterCallback filter, DatabaseCompletionCallback<List<MealerComplaint>> callback);

    /**
     * Update {@link MealerRecipe} stored in database
     * @param recipe
     * @return
     */
    default Future<Boolean> updateRecipe(MealerRecipe recipe) {
        return updateRecipe(recipe, null);
    }

    /**
     * Update a {@link MealerRecipe} stored in Database.
     * Executes callback on completion.
     * @param recipe new version of recipe
     * @param callback code to execute on completion
     * @return
     */
    Future<Boolean> updateRecipe(MealerRecipe recipe, DatabaseSetCallback callback);

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
     * Delete a {@link MealerRecipe} stored in Database.
     *
     * @param id
     * @return
     */
    default Future<Boolean> deleteRecipe(String id) {
        return deleteRecipe(id, null);
    }

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
     * Delete a {@link MealerComplaint} stored in Database.
     * Executes callback on completion.
     *
     * @param id
     * @return
     */
    default Future<Boolean> deleteComplaint(String id) {
        return deleteComplaint(id, null);
    }

    /**
     * Delete a {@link MealerComplaint} stored in Database.
     * Executes callback on completion.
     *
     * @param id
     * @param callback
     * @return
     */
    Future<Boolean> deleteComplaint(String id, DatabaseSetCallback callback);

    /**
     * Return true if sign in user's info needs to be updated
     *
     * @return
     */
    default Future<Boolean> userInfoRequired() {
        return userInfoRequired(null);
    }

    /**
     * Return true if sign in user's info needs to be updated
     * Executes callback on completion.
     *
     * @return
     */
    Future<Boolean> userInfoRequired(DatabaseCompletionCallback<Boolean> callback);

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
    <T extends MealerSerializable> void listenForModel(Activity activity, String collectionId, String documentId, Class<T> clazz, DatabaseCompletionCallback<T> callback);

    <T extends MealerSerializable> void listenForModels(Activity activity, String collectionId, Class<T> clazz, DatabaseFilterCallback filter, DatabaseCompletionCallback<List<T>> callback);

    default <T extends MealerSerializable> void listenForModels(Activity activity, String collectionId, Class<T> clazz, DatabaseCompletionCallback<List<T>> callback){
        listenForModels(activity, collectionId, clazz, object -> object.limit(100), callback);
    }

    Future<Boolean> updateComplaint(MealerComplaint complaint, DatabaseSetCallback callback);

    default Future<Boolean> updateComplaint(MealerComplaint complaint) {
        return updateComplaint(complaint, null);
    }
}
