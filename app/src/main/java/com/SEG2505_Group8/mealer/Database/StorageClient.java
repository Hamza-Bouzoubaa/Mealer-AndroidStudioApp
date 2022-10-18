package com.SEG2505_Group8.mealer.Database;

import com.SEG2505_Group8.mealer.Database.Callbacks.StorageProgressCallback;

import java.util.concurrent.Future;

public interface StorageClient {
    /**
     * Upload an array of bytes to desired path.
     * Requires all bytes to be loaded in memory. Consider using uploadFile(String remotePath, String localPath) to save memory
     * @param remotePath of file on server
     * @param bytes to upload to remotePath
     * @return
     */
    Future<Boolean> uploadFile(byte[] bytes, String remotePath, StorageProgressCallback callback);
}
