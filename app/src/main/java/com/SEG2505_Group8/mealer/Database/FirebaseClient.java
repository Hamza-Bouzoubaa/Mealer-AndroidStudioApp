package com.SEG2505_Group8.mealer.Database;

import com.SEG2505_Group8.mealer.Database.Models.MealerMenu;
import com.SEG2505_Group8.mealer.Database.Models.MealerRecipe;
import com.SEG2505_Group8.mealer.Database.Models.MealerUser;
import com.SEG2505_Group8.mealer.Database.Serialize.MealerSerializer;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseClient implements DatabaseClient {

    private static final String userCollectionId = "users";

    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    public MealerUser getUser(String id) {
        return null;
    }

    @Override
    public MealerUser getUser() {
        return null;
    }

    @Override
    public MealerMenu getUserMenu(String id) {
        return null;
    }

    @Override
    public MealerMenu getUserMenu() {
        return null;
    }

    @Override
    public MealerMenu getMenu(String id) {
        return null;
    }

    @Override
    public MealerRecipe getRecipe(String id) {
        return null;
    }

    @Override
    public Task<Void> updateRecipe(MealerRecipe recipe) {
        return null;
    }

    @Override
    public Task<Void> updateMenu(MealerMenu menu) {
        return null;
    }

    @Override
    public Task<Void> updateUser(MealerUser user) {

        return firestore.collection(userCollectionId).document(user.getId()).set(MealerSerializer.toMap(user));

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
}