package com.SEG2505_Group8.mealer.Database;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.SEG2505_Group8.mealer.Database.Models.MealerOrder;
import com.SEG2505_Group8.mealer.Database.Models.MealerOrderStatus;
import com.SEG2505_Group8.mealer.Database.Utils.DatabaseFilterCallback;
import com.SEG2505_Group8.mealer.Database.Utils.DatabaseCompletionCallback;
import com.SEG2505_Group8.mealer.Database.Utils.DatabaseListener;
import com.SEG2505_Group8.mealer.Database.Utils.DatabaseSetCallback;
import com.SEG2505_Group8.mealer.Database.Models.MealerComplaint;
import com.SEG2505_Group8.mealer.Database.Models.MealerMenu;
import com.SEG2505_Group8.mealer.Database.Models.MealerRecipe;
import com.SEG2505_Group8.mealer.Database.Models.MealerUser;
import com.SEG2505_Group8.mealer.Database.Serialize.MealerSerializable;
import com.SEG2505_Group8.mealer.Database.Utils.MealerMessager;
import com.SEG2505_Group8.mealer.UI.Activities.Utils.DateUtils;
import com.google.common.util.concurrent.SettableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Future;

public class FirebaseDatabaseClient implements DatabaseClient {

    private static final int maxiumGetModels = 20;

    private static final String userCollectionId = "users";
    private static final String menuCollectionId = "menus";
    private static final String recipeCollectionId = "recipes";
    private static final String complaintCollectionId = "complaints";
    private static final String orderCollectionId = "orders";

    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    public <T extends MealerUser> Future<T> getUser(String id, Class<T> clazz, DatabaseCompletionCallback<T> callback) throws NullPointerException {

        try {
            if (id == null || id.isEmpty()) {
                id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            }
        } catch (NullPointerException e) {
            SettableFuture<T> f = SettableFuture.create();
            f.set(null);
            return f;
        }

        return getModel(userCollectionId, id, clazz, callback);
    }

    @Override
    public Future<MealerMenu> getUserMenu(String id, DatabaseCompletionCallback<MealerMenu> callback) throws NullPointerException {

        if (id == null || id.isEmpty()) {
            id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        }

        final String userId = id;

        // Create a future.
        final SettableFuture<MealerMenu> future = SettableFuture.create();

        getUser(userId, user -> {
            getMenu(user.getMenuId(), menu -> {
                callback.onComplete(menu);
                future.set(menu);
            });
        });

        return future;
    }

    @Override
    public Future<MealerOrder> getOrder(String id, DatabaseCompletionCallback<MealerOrder> callback) {
        return getModel(orderCollectionId, id, MealerOrder.class, callback);
    }

    @Override
    public Future<MealerMenu> getMenu(String id, DatabaseCompletionCallback<MealerMenu> callback) {
        return getModel(menuCollectionId, id, MealerMenu.class, callback);
    }


    @Override
    public Future<MealerRecipe> getRecipe(String id, DatabaseCompletionCallback<MealerRecipe> callback) {
        return getModel(recipeCollectionId, id, MealerRecipe.class, callback);
    }

    @Override
    public Future<List<MealerRecipe>> searchRecipes(int limit, DatabaseFilterCallback filter, DatabaseCompletionCallback<List<MealerRecipe>> callback) {
        return getModels(MealerRecipe.class, recipeCollectionId, limit, filter, callback);
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
    public Future<Boolean> updateOrder(MealerOrder order, DatabaseSetCallback callback) {
        return saveModel(orderCollectionId, order.getId(), order, callback);
    }

    @Override
    public Future<MealerOrder> orderRecipe(MealerRecipe recipe, DatabaseCompletionCallback<MealerOrder> callback) {

        String clientId = null;
        try {
            clientId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        SettableFuture<MealerOrder> future = SettableFuture.create();

        MealerOrder order = new MealerOrder(UUID.randomUUID().toString(), MealerOrderStatus.WAITING, recipe.getChefId(), clientId, recipe.getId());

        updateOrder(order, success -> {
            if (success) {
                getUser(user -> {

                    if (user.getTotalSales() == null) {
                        user.setTotalSales(0);
                    }

                   user.setTotalSales(user.getTotalSales() + 1);

                    updateUser(user, s -> {
                        callback.onComplete(s ? order : null);
                        future.set(s ? order : null);
                    });
                });
            } else {
                callback.onComplete(null);
                future.set(null);
            }
        });

        return future;
    }

    @Override
    public Future<Boolean> deleteRecipe(String id, DatabaseSetCallback callback) {
        return deleteModel(recipeCollectionId, id, callback);
    }

    @Override
    public Future<Boolean> deleteOrder(String id, DatabaseSetCallback callback) {
        return deleteModel(orderCollectionId, id, callback);
    }

    @Override
    public Future<Boolean> deleteComplaint(String id, DatabaseSetCallback callback) {
        return deleteModel(complaintCollectionId, id, callback);
    }

    @Override
    public Future<Boolean> updateUserToken() {
        SettableFuture<Boolean> future = SettableFuture.create();
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(token -> {
            getUser(u -> {
               u.setMessageToken(token.getResult());
               System.out.println(token.getResult());
               saveModel(userCollectionId, FirebaseAuth.getInstance().getUid(), u, future::set);
            });
        });
        return future;
    }

    @Override
    public Future<Boolean> rejectOrder(MealerOrder order, Context context, DatabaseSetCallback callback) {

        Future<Boolean> future = SettableFuture.create();

        order.setStatus(MealerOrderStatus.REJECTED);
        MealerMessager messager = new MealerMessager(context);

        getUser(order.getClientId(), u -> {
            messager.notifyClientOrder(order.getId(), u.getMessageToken());
            saveModel(orderCollectionId, order.getId(), order, callback);
        });
        return future;
    }

    @Override
    public DatabaseListener listenForUserRecipes(Activity activity, String userId, boolean offeredRecipesOnly, DatabaseCompletionCallback<List<MealerRecipe>> callback) {
        DatabaseListener listener = new DatabaseListener();

        listener.addRegistration(listenForModel(activity, userCollectionId, userId, MealerUser.class, user -> {

            if (user == null) {
                callback.onComplete(null);
                return;
            }

            listener.addRegistration(listenForModel(activity, menuCollectionId, user.getMenuId(), MealerMenu.class, menu -> {

                if (menu == null) {
                    callback.onComplete(null);
                    return;
                }

                listener.addRegistration(listenForModels(activity, recipeCollectionId, MealerRecipe.class, reference -> {
                    if (menu == null || menu.getRecipeIds() == null || menu.getRecipeIds().isEmpty()) {
                        return reference.whereEqualTo(FieldPath.documentId(), "invalid");
                    }

                    Query q = reference.whereIn(FieldPath.documentId(), menu.getRecipeIds());

                    if (offeredRecipesOnly) {
                        q = q.whereEqualTo("isOffered", true);
                    }

                    return q;
                }, callback));
            }));
        }));

        return listener;
    }

    @Override
    public <T extends MealerSerializable> DatabaseListener listenForModel(Activity activity, String collectionId, String documentId, Class<T> clazz, DatabaseCompletionCallback<T> callback) {

        DocumentReference reference = firestore.collection(collectionId).document(documentId);
        ListenerRegistration registration = reference.addSnapshotListener(activity, (documentSnapshot, e) -> {
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

        return new DatabaseListener(registration);
    }

    @Override
    public <T extends MealerSerializable> DatabaseListener listenForModels(Activity activity, String collectionId, Class<T> clazz, DatabaseFilterCallback filter, DatabaseCompletionCallback<List<T>> callback) {

        CollectionReference reference = firestore.collection(collectionId);

        ListenerRegistration registration = filter.applyFilter(reference).addSnapshotListener(activity, (querySnapshot, e) -> {
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

        return new DatabaseListener(registration);
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
                if (callback != null) {
                    callback.onComplete(true);
                }
                future.set(true);
            } else {
                if (callback != null) {
                    callback.onComplete(false);
                }
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

                if (callback != null) {
                    callback.onComplete(o);
                }

                future.set(o);
            } else {
                    // Firebase task wasn't successful, set future null
                if (callback != null) {
                    callback.onComplete(null);
                }
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