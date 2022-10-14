package com.SEG2505_Group8.mealer;

import com.SEG2505_Group8.mealer.Database.DatabaseClient;
import com.SEG2505_Group8.mealer.Database.FirebaseDatabaseClient;
import com.SEG2505_Group8.mealer.Database.FirebaseStorageClient;
import com.SEG2505_Group8.mealer.Database.StorageClient;

public class Services {
    static private final DatabaseClient databaseClient = new FirebaseDatabaseClient();
    static private final StorageClient storageClient = new FirebaseStorageClient();

    static public DatabaseClient getDatabaseClient() {
        return databaseClient;
    }
    static public StorageClient getStorageClient() {
        return storageClient;
    }
}
