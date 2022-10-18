package com.SEG2505_Group8.mealer.Database.Callbacks;

public interface DatabaseCompletionCallback {
    <T> void onComplete(T object);
}
