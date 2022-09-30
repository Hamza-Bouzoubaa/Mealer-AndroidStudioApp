package com.SEG2505_Group8.mealer.Database;

import com.SEG2505_Group8.mealer.Database.Models.MealerMenu;
import com.SEG2505_Group8.mealer.Database.Models.MealerRecipe;
import com.SEG2505_Group8.mealer.Database.Models.MealerUser;
import com.SEG2505_Group8.mealer.Database.Serialize.MealerSerializer;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.Objects;

public class FirebaseClient implements DatabaseClient {

    private static final String userCollectionId = "users";
    private static final String menuCollectionId = "menus";
    private static final String recipeCollectionId = "recipes";

    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    public Task<MealerUser> getUser(String id) {
        return null;
    }

    @Override
    public Task<MealerUser> getUser() throws NullPointerException {
        return null;
    }

    @Override
    public Task<MealerMenu> getUserMenu(String id) {
        return null;
    }

    @Override
    public Task<MealerMenu> getUserMenu() {
        return null;
    }

    @Override
    public Task<MealerMenu> getMenu(String id) {
        return null;
    }

    @Override
    public Task<MealerRecipe> getRecipe(String id) {
        return null;
    }

    /**
     * Wrapper for the saveModel method.
     * Automatically sets collection and document id.
     *
     * @param recipe
     * @return
     */
    @Override
    public Task<Void> updateRecipe(MealerRecipe recipe) {
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
    public Task<Void> updateMenu(MealerMenu menu) {
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
    public Task<Void> updateUser(MealerUser user) {
        return saveModel(userCollectionId, user.getId(), user);
    }

    /**
     * Lookup a user in the database and check if we need to ask for more data.
     * Ex: Is there address missing?
     *
     * @param user
     * @return
     */
    @Override
    public boolean userDataInputRequired(MealerUser user) {
        return false;
    }

    /**
     * https://www.linkedin.com/pulse/another-layer-abstraction-firebase-real-time-database-soham-sengupta
     *
     * @param collectionName
     * @param objToSave
     * @return
     */
    private Task<Void> saveModel(String collectionName, String documentId, Object objToSave) {

        if (!MealerSerializer.isSerializable(objToSave)) {
            throw new IllegalArgumentException("Object to Save is not Mealer Serializable!");
        }

        Objects.requireNonNull(collectionName, "No collection name specified!");
        CollectionReference ref = firestore.collection(collectionName);

        Map<String, Object> mappedData = MealerSerializer.toMap(objToSave);

        if (mappedData == null) {
            throw new IllegalArgumentException("Mapped data is null!");
        }

        return ref.document(documentId).set(mappedData);
    }
}