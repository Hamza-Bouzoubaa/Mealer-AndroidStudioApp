package com.SEG2505_Group8.mealer.Database.Utils;

/**
 * Interface for database callbacks
 * Used to create lambda functions.
 * @param <T>
 */
public interface DatabaseCompletionCallback<T> {
    void onComplete(T object);
}
