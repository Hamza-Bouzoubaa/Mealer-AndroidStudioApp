package com.SEG2505_Group8.mealer.Database.Callbacks;

/**
 * Interface for database SET operations.
 * Used to execute code once a set is complete.
 */
public interface DatabaseSetCallback {
    void onComplete(Boolean object);
}
