package com.SEG2505_Group8.mealer;

import com.SEG2505_Group8.mealer.Database.DatabaseClient;
import com.SEG2505_Group8.mealer.Database.FirebaseClient;

public class Services {
    static private final DatabaseClient databaseClient = new FirebaseClient();

    static public DatabaseClient getDatabaseClient() {
        return databaseClient;
    }
}
