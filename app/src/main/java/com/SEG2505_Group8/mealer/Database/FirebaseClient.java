package com.SEG2505_Group8.mealer.Database;

import com.SEG2505_Group8.mealer.Database.Models.MealerMenu;
import com.SEG2505_Group8.mealer.Database.Models.MealerRecipe;
import com.SEG2505_Group8.mealer.Database.Models.MealerUser;
import com.SEG2505_Group8.mealer.Database.Serialize.MealerSerializer;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FirebaseClient implements DatabaseClient {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(executorService);

    private static final String userCollectionId = "users";
    private static final String menuCollectionId = "menus";
    private static final String recipeCollectionId = "recipes";

    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    public Future<MealerUser> getUser(String id) {
        return getModel(userCollectionId, id, MealerUser.class);
    }

    @Override
    public Future<MealerUser> getUser() throws NullPointerException {
        // Get the logged in user's id
        String id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        return getUser(id);
    }

    @Override
    public Future<MealerMenu> getUserMenu(String id) {
        // Create a future.
        final SettableFuture<MealerMenu> future = SettableFuture.create();

        // Get the user, then get their menu
        executorService.submit(() -> {
            try {
                MealerUser user = getUser(id).get();
                MealerMenu menu = getMenu(user.getMenuId()).get();
                future.set(menu);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                future.setException(e);
            }
        });

        return future;
    }

    @Override
    public Future<MealerMenu> getUserMenu() {
        // Get the logged in user's id
        String id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        return getUserMenu(id);
    }

    @Override
    public Future<MealerMenu> getMenu(String id) {
        return getModel(menuCollectionId, id, MealerMenu.class);
    }

    @Override
    public Future<MealerRecipe> getRecipe(String id) {
        return getModel(recipeCollectionId, id, MealerRecipe.class);
    }

    /**
     * Wrapper for the saveModel method.
     * Automatically sets collection and document id.
     *
     * @param recipe
     * @return
     */
    @Override
    public Future<Void> updateRecipe(MealerRecipe recipe) {
        return saveModel(recipeCollectionId, recipe.getId(), recipe);
    }

    /**
     * Wrapper for the saveModel method.
     * Automatically sets collection and document id.
     *
     * @param menu
     * @return
     */
    @Override
    public Future<Void> updateMenu(MealerMenu menu) {
        return saveModel(menuCollectionId, menu.getId(), menu);
    }

    /**
     * Wrapper for the saveModel method.
     * Automatically sets collection and document id.
     *
     * @param user
     * @return
     */
    @Override
    public Future<Void> updateUser(MealerUser user) {
        return saveModel(userCollectionId, user.getId(), user);
    }

    /**
     * Lookup the user in the database and check if we need to ask for more data.
     * Ex: Is there address missing?
     *
     * @return
     */
    @Override
    public Future<Boolean> userInfoRequired() {

        //TODO: Implement logic for user info missing fields
        SettableFuture<Boolean> future = SettableFuture.create();
        future.set(true);
        return future;
    }

    /**
     * https://www.linkedin.com/pulse/another-layer-abstraction-firebase-real-time-database-soham-sengupta
     *
     * @param collectionName
     * @param objToSave
     * @return
     */
    private Future<Void> saveModel(String collectionName, String documentId, Object objToSave) {

        final SettableFuture<Void> future = SettableFuture.create();

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

        executorService.submit(() -> {
            firestore.collection(collectionName).document(documentId).set(mappedData).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    future.set(null);
                } else {
                    future.setException(new Exception("Failed to save data to firestore!"));
                }
            });
        });


        return future;
    }

    private <T> Future<T> getModel(String collectionId, String documentId, Class<T> clazz) {

        // Create a future.
        final SettableFuture<T> future = SettableFuture.create();

        // Create an async task to fetch user from firestore
        executorService.submit(() -> {
            firestore.collection(collectionId).document(documentId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Firebase task was successful, set future to result
                    future.set(task.getResult().toObject(clazz));
                } else {
                    // Firebase task wasn't successful, set future null
                    future.set(null);
                }
            });
        });

        return future;
    }
}