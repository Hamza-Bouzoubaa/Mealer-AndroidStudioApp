package com.SEG2505_Group8.mealer.Database.Callbacks;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;

/**
 * Interface for filtering database data.
 * Used to create lambda functions.
 */
public interface DatabaseFilterCallback {
    Query applyFilter(CollectionReference reference);
}
