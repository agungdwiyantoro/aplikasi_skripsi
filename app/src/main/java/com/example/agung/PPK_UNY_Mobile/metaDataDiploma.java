package com.example.agung.PPK_UNY_Mobile;

import android.content.Context;

import com.google.firebase.storage.StorageMetadata;

import java.lang.ref.WeakReference;

class metaDataDiploma {
    private String fullName, NIM, phoneNumber, fileName;
    private WeakReference<Context> context;

    metaDataDiploma(Context context, String fullName, String NIM, String phoneNumber, String fileName) {
        this.context = new WeakReference<>(context);
        this.fullName = fullName;
        this.NIM = NIM;
        this.phoneNumber = phoneNumber;
        this.fileName = fileName;
    }

    public metaDataDiploma(Context context, String phoneNumber) {
        this.context = new WeakReference<>(context);
        this.phoneNumber = phoneNumber;
    }

    StorageMetadata storageMetaData(){
        return new StorageMetadata.Builder()
                .setContentEncoding("UTF-8")
                .setCustomMetadata(context.get().getResources().getString(R.string.fullName), fullName)
                .setCustomMetadata(context.get().getResources().getString(R.string.NIM), NIM)
                .setCustomMetadata(context.get().getResources().getString(R.string.phoneNumber), phoneNumber)
                .setCustomMetadata(context.get().getResources().getString(R.string.key_diploma_name), fileName)
                .build();
    }

    StorageMetadata storageMetaDataPhoneNumber(){
        return new StorageMetadata.Builder()
                .setContentEncoding("UTF-8")
                .setCustomMetadata(context.get().getResources().getString(R.string.phoneNumber), phoneNumber)
                .build();
    }
}
