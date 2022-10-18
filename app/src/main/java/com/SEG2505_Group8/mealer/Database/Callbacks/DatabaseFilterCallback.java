package com.SEG2505_Group8.mealer.Database.Callbacks;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;

public interface DatabaseFilterCallback {
    Query applyFilter(CollectionReference reference);
}
