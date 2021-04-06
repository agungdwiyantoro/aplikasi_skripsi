package com.example.agung.PPK_UNY_Mobile;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

public class updateMetaData extends AsyncTask<String, Void, Void> {

    private StorageReference storageReference;
    private StorageMetadata metaData;

    updateMetaData(StorageReference storageReference, StorageMetadata metaData){
        this.storageReference = storageReference;
        this.metaData = metaData;
    }
    @Override
    protected Void doInBackground(String... strings) {
        final String TAG = "updateMetaData";

        storageReference.updateMetadata(metaData).addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                Log.d(TAG, "Update is successfull");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Update is Failed");
            }
        });

        return null;
    }

}