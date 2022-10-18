package com.SEG2505_Group8.mealer.Database;

import com.SEG2505_Group8.mealer.Database.Callbacks.DatabaseFilterCallback;
import com.SEG2505_Group8.mealer.Database.Callbacks.DatabaseCompletionCallback;
import com.SEG2505_Group8.mealer.Database.Callbacks.DatabaseSetCallback;
import com.SEG2505_Group8.mealer.Database.Models.MealerMenu;
import com.SEG2505_Group8.mealer.Database.Models.MealerRecipe;
import com.SEG2505_Group8.mealer.Database.Models.MealerUser;
import com.SEG2505_Group8.mealer.Database.Serialize.MealerSerializer;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.SettableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.WriteResult;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FirebaseDatabaseClient implements DatabaseClient {

    private static final String userCollectionId = "users";
    private static final String menuCollectionId = "menus";
    private static final String recipeCollectionId = "recipes";

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    public Future<MealerUser> getUser(String id, DatabaseCompletionCallback callback) throws NullPointerException {
        if (id == null || id.isEmpty()) {
            id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        }

        return getModel(userCollectionId, id, MealerUser.class, callback);
    }

    @Override
    public Future<List<MealerUser>> getUsers(DatabaseFilterCallback filter) {
        return getModels(MealerUser.class, userCollectionId, 10, reference -> {
            return reference;
        });
    }

    @Override
    public Future<MealerMenu> getUserMenu(String id, DatabaseCompletionCallback callback) throws NullPointerException {

        if (id == null || id.isEmpty()) {
            id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        }

        final String userId = id;

        // Create a future.
        final SettableFuture<MealerMenu> future = SettableFuture.create();

        // Get the user, then get their menu
        executorService.submit(() -> {
            try {
                MealerUser user = getUser(userId).get();
                MealerMenu menu = getMenu(user.getMenuId(), callback).get();
                future.set(menu);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                future.setException(e);
            }
        });

        return future;
    }

    @Override
    public Future<List<MealerMenu>> getMenus(DatabaseFilterCallback filter) {
        return getModels(MealerMenu.class, menuCollectionId, 10, reference -> {
            return reference;
        });
    }

    @Override
    public Future<MealerMenu> getMenu(String id, DatabaseCompletionCallback callback) {
        return getModel(menuCollectionId, id, MealerMenu.class, callback);
    }

    @Override
    public Future<List<MealerRecipe>> getRecipes(DatabaseFilterCallback filter) {
        return getModels(MealerRecipe.class, recipeCollectionId, 10, filter);
    }

    @Override
    public Future<MealerRecipe> getRecipe(String id, DatabaseCompletionCallback callback) {
        return getModel(recipeCollectionId, id, MealerRecipe.class, callback);
    }

    /**
     * Wrapper for the saveModel method.
     * Automatically sets collection and document id.
     *
     * @param recipe
     * @return
     */
    @Override
    public Future<Boolean> updateRecipe(MealerRecipe recipe, DatabaseSetCallback callback) {
        return saveModel(recipeCollectionId, recipe.getId(), recipe, callback);
    }

    /**
     * Wrapper for the saveModel method.
     * Automatically sets collection and document id.
     *
     * @param menu
     * @return
     */
    @Override
    public Future<Boolean> updateMenu(MealerMenu menu, DatabaseSetCallback callback) {
        return saveModel(menuCollectionId, menu.getId(), menu, callback);
    }

    /**
     * Wrapper for the saveModel method.
     * Automatically sets collection and document id.
     *
     * @param user
     * @return
     */
    @Override
    public Future<Boolean> updateUser(MealerUser user, DatabaseSetCallback callback) {
        return saveModel(userCollectionId, user.getId(), user, callback);
    }

    @Override
    public Future<Boolean> deleteRecipe(String id, DatabaseCompletionCallback callback) {
        return deleteModel(recipeCollectionId, id, callback);
    }

    /**
     * Lookup the user in the database and check if we need to ask for more data.
     * Ex: Is there address missing?
     *
     * @return
     */
    @Override
    public Future<Boolean> userInfoRequired(DatabaseCompletionCallback callback) {

        //TODO: Implement logic for user info missing fields. We currently only check if document exists.
        SettableFuture<Boolean> future = SettableFuture.create();

//        executorService.submit(() -> {
//            firestore.collection(userCollectionId).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    future.set(!task.getResult().exists());
//                } else {
//                    future.set(false);
//                }
//            });
//        });
        callback.onComplete(true);
        future.set(true);

        return future;
    }

    private <T> Future<List<T>> getModels(Class<T> clazz, String collectionName, int limit, DatabaseFilterCallback filter) {
        final SettableFuture<List<T>> future = SettableFuture.create();

        filter.applyFilter(firestore.collection(collectionName)).limit(limit).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                future.set(task.getResult().toObjects(clazz));
            } else {
                future.set(null);
            }
        });

        return future;
    }

        /**
         * https://www.linkedin.com/pulse/another-layer-abstraction-firebase-real-time-database-soham-sengupta
         *
         * @param collectionName
         * @param objToSave
         * @return
         */
    private Future<Boolean> saveModel(String collectionName, String documentId, Object objToSave, DatabaseSetCallback callback) {

        final SettableFuture<Boolean> future = SettableFuture.create();

        if (!MealerSerializer.isSerializable(objToSave)) {
            future.setException(new IllegalArgumentException("Object to Save is not Mealer Serializable!"));
            return future;
        }

        if (collectionName == null) {
            future.setException(new IllegalArgumentException("No collection name specified!"));
            return future;
        }

        Map<String, Object> mappedData = MealerSerializer.toMap(objToSave);

        if (mappedData == null) {
            future.setException(new IllegalArgumentException("Mapped data is null!"));
            return future;
        }

        firestore.collection(collectionName).document(documentId).set(mappedData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                future.set(true);
            } else {
                future.setException(new Exception("Failed to save data to firestore!"));
                future.set(false);
            }
        });


        return future;
    }

    private <T> Future<T> getModel(String collectionId, String documentId, Class<T> clazz, DatabaseCompletionCallback callback) {

        // Create a future.
        final SettableFuture<T> future = SettableFuture.create();

        // Create an async task to fetch user from firestore
        executorService.submit(() -> {
            firestore.collection(collectionId).document(documentId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Firebase task was successful, set future to result
                    T o = task.getResult().toObject(clazz);
                    callback.onComplete(o);
                    future.set(o);
                } else {
                    // Firebase task wasn't successful, set future null
                    callback.onComplete(null);
                    future.set(null);
                }
            });
        });

        return future;
    }

    private Future<Boolean> deleteModel(String collectionId, String documentId, DatabaseCompletionCallback callback) {
        final SettableFuture<Boolean> future = SettableFuture.create();

        firestore.collection(collectionId).document(documentId).delete().addOnCompleteListener(task -> {
            future.set(task.isSuccessful());
        });

        return future;
    }
}