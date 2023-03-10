package com.SEG2505_Group8.mealer.Database.Utils;

/**
 * Interface for executing code as storage operation progresses
 */
public interface StorageProgressCallback {
    void onProgress(long processed, long total);
}
