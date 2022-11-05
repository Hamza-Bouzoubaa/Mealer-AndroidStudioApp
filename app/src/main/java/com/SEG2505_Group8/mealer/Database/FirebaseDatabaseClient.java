package com.SEG2505_Group8.mealer.Database;

import android.app.Activity;
import android.widget.Toast;

import com.SEG2505_Group8.mealer.Database.Callbacks.DatabaseFilterCallback;
import com.SEG2505_Group8.mealer.Database.Callbacks.DatabaseCompletionCallback;
import com.SEG2505_Group8.mealer.Database.Callbacks.DatabaseSetCallback;
import com.SEG2505_Group8.mealer.Database.Models.MealerComplaint;
import com.SEG2505_Group8.mealer.Database.Models.MealerMenu;
import com.SEG2505_Group8.mealer.Database.Models.MealerRecipe;
import com.SEG2505_Group8.mealer.Database.Models.MealerUser;
import com.SEG2505_Group8.mealer.Database.Serialize.MealerSerializable;
import com.google.common.util.concurrent.SettableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FirebaseDatabaseClient implements DatabaseClient {

    private static final int maxiumGetModels = 20;

    private static final String userCollectionId = "users";
    private static final String menuCollectionId = "menus";
    private static final String recipeCollectionId = "recipes";
    private static final String complaintCollectionId = "complaints";

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    public Future<MealerUser> getUser(String id, DatabaseCompletionCallback<MealerUser> callback) throws NullPointerException {
        if (id == null || id.isEmpty()) {
            id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        }

        return getModel(userCollectionId, id, MealerUser.class, callback);
    }

    @Override
    public Future<List<MealerUser>> getUsers(DatabaseFilterCallback filter) {
        return getModels(MealerUser.class, userCollectionId, maxiumGetModels, reference -> {
            return reference;
        }, null);
    }

    @Override
    public Future<MealerMenu> getUserMenu(String id, DatabaseCompletionCallback<MealerMenu> callback) throws NullPointerException {

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
        return getModels(MealerMenu.class, menuCollectionId, maxiumGetModels, reference -> {
            return reference;
        }, null);
    }

    @Override
    public Future<MealerMenu> getMenu(String id, DatabaseCompletionCallback<MealerMenu> callback) {
        return getModel(menuCollectionId, id, MealerMenu.class, callback);
    }

    @Override
    public Future<List<MealerRecipe>> getRecipes(DatabaseFilterCallback filter) {
        return getModels(MealerRecipe.class, recipeCollectionId, maxiumGetModels, filter, null);
    }

    @Override
    public Future<MealerRecipe> getRecipe(String id, DatabaseCompletionCallback<MealerRecipe> callback) {
        return getModel(recipeCollectionId, id, MealerRecipe.class, callback);
    }

    @Override
    public Future<List<MealerComplaint>> getComplaints(DatabaseFilterCallback filter, DatabaseCompletionCallback<List<MealerComplaint>> callback) {
        return getModels(MealerComplaint.class, complaintCollectionId, maxiumGetModels, filter, callback);
    }

    @Override
    public Future<Boolean> updateRecipe(MealerRecipe recipe, DatabaseSetCallback callback) {
        return saveModel(recipeCollectionId, recipe.getId(), recipe, callback);
    }

    @Override
    public Future<Boolean> updateMenu(MealerMenu menu, DatabaseSetCallback callback) {
        return saveModel(menuCollectionId, menu.getId(), menu, callback);
    }

    @Override
    public Future<Boolean> updateUser(MealerUser user, DatabaseSetCallback callback) {
        return saveModel(userCollectionId, user.getId(), user, callback);
    }

    @Override
    public Future<Boolean> deleteRecipe(String id, DatabaseSetCallback callback) {
        return deleteModel(recipeCollectionId, id, callback);
    }

    @Override
    public Future<Boolean> deleteComplaint(String id, DatabaseSetCallback callback) {
        return deleteModel(complaintCollectionId, id, callback);
    }

    @Override
    public Future<Boolean> userInfoRequired(DatabaseCompletionCallback<Boolean> callback) {

        //TODO: Implement logic for user info missing fields. We currently only check if document exists.
        SettableFuture<Boolean> future = SettableFuture.create();

        // Default to true until implementation is written
        callback.onComplete(true);
        future.set(true);

        return future;
    }

    @Override
    public <T extends MealerSerializable> void listenForModel(Activity activity, String collectionId, String documentId, Class<T> clazz, DatabaseCompletionCallback<T> callback) {

        DocumentReference reference = firestore.collection(collectionId).document(documentId);
        reference.addSnapshotListener(activity, (documentSnapshot, e) -> {
            if(e != null){
                Toast.makeText(activity, "Error Loading", Toast.LENGTH_SHORT).show();
                return;
            }
            if(documentSnapshot.exists()){
                T o = documentSnapshot.toObject(clazz);

                if (o != null) {
                    o.setId(documentId);
                }

                callback.onComplete(o);
            }
        });
    }

    @Override
    public <T extends MealerSerializable> void listenForModels(Activity activity, String collectionId, Class<T> clazz, DatabaseFilterCallback filter, DatabaseCompletionCallback<List<T>> callback) {

        CollectionReference reference = firestore.collection(collectionId);

        filter.applyFilter(reference).addSnapshotListener(activity, (querySnapshot, e) -> {
            if(e != null){
                Toast.makeText(activity, "Error Loading", Toast.LENGTH_SHORT).show();
                return;
            }

            List<T> o = new ArrayList<>();// = task.getResult().toObjects(clazz);
            querySnapshot.getDocuments().forEach(documentSnapshot -> {
                T doc = documentSnapshot.toObject(clazz);

                if (doc != null) {
                    doc.setId(documentSnapshot.getId());
                    o.add(doc);
                }
            });

            callback.onComplete(o);
        });
    }

    @Override
    public Future<Boolean> updateComplaint(MealerComplaint complaint, DatabaseSetCallback callback) {
        return saveModel(complaintCollectionId, complaint.getId(), complaint, callback);
    }

    /**
     * Get list of objects of type T from firebase
     * @param clazz
     * @param collectionName
     * @param limit
     * @param filter
     * @param <T>
     * @return
     */
    private <T extends MealerSerializable> Future<List<T>> getModels(Class<T> clazz, String collectionName, int limit, DatabaseFilterCallback filter, DatabaseCompletionCallback<List<T>> callback) {
        final SettableFuture<List<T>> future = SettableFuture.create();

        filter.applyFilter(firestore.collection(collectionName)).limit(limit).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<T> o = new ArrayList<>();// = task.getResult().toObjects(clazz);
                task.getResult().getDocuments().forEach(documentSnapshot -> {
                    T doc = documentSnapshot.toObject(clazz);

                    if (doc != null) {
                        doc.setId(documentSnapshot.getId());
                        o.add(doc);
                    }
                });

                callback.onComplete(o);
                future.set(o);
            } else {
                callback.onComplete(null);
                future.set(null);
            }
        });

        return future;
    }

    /**
     * Save object to firebase.
     * @param collectionName
     * @param documentId
     * @param objToSave
     * @param callback
     * @return
     */
    private Future<Boolean> saveModel(String collectionName, String documentId, MealerSerializable objToSave, DatabaseSetCallback callback) {

        final SettableFuture<Boolean> future = SettableFuture.create();

        if (collectionName == null) {
            future.setException(new IllegalArgumentException("No collection name specified!"));
            return future;
        }

        Map<String, Object> mappedData = objToSave.toMap();

        if (mappedData == null) {
            future.setException(new IllegalArgumentException("Mapped data is null!"));
            return future;
        }

        if (documentId == null) {
            documentId = UUID.randomUUID().toString();
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

    /**
     * Get object of type T from firebase.
     * @param collectionId
     * @param documentId
     * @param clazz
     * @param callback
     * @param <T>
     * @return
     */
    private <T extends MealerSerializable> Future<T> getModel(String collectionId, String documentId, Class<T> clazz, DatabaseCompletionCallback<T> callback) {

        // Create a future.
        final SettableFuture<T> future = SettableFuture.create();

        // Create an async task to fetch user from firestore
        firestore.collection(collectionId).document(documentId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Firebase task was successful, set future to result
                T o = task.getResult().toObject(clazz);

                if (o == null) {
                    callback.onComplete(null);
                    future.set(null);
                    return;
                }

                o.setId(documentId);
                callback.onComplete(o);
                future.set(o);
            } else {
                    // Firebase task wasn't successful, set future null
                callback.onComplete(null);
                future.set(null);
            }
        });

        return future;
    }

    /**
     * Delete model from firebase
     * @param collectionId
     * @param documentId
     * @param callback
     * @return
     */
    private Future<Boolean> deleteModel(String collectionId, String documentId, DatabaseSetCallback callback) {
        final SettableFuture<Boolean> future = SettableFuture.create();

        firestore.collection(collectionId).document(documentId).delete().addOnCompleteListener(task -> {
            if (callback != null) {
                callback.onComplete(task.isSuccessful());
            }
            future.set(task.isSuccessful());
        });

        return future;
    }
}