package com.SEG2505_Group8.mealer.Database;

import com.SEG2505_Group8.mealer.Database.Utils.StorageProgressCallback;
import com.google.common.util.concurrent.SettableFuture;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.concurrent.Future;

public class FirebaseStorageClient implements StorageClient {

    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageRef = storage.getReference();

    @Override
    public Future<Boolean> uploadFile(byte[] bytes, String remotePath, StorageProgressCallback callback) {
        SettableFuture<Boolean> future = SettableFuture.create();

        UploadTask uploadTask = storageRef.child(remotePath).putBytes(bytes);
        uploadTask.addOnCompleteListener(task -> future.set(task.isSuccessful()));
        uploadTask.addOnProgressListener(snapshot -> callback.onProgress(snapshot.getBytesTransferred(), snapshot.getTotalByteCount()));

        return future;
    }
}
