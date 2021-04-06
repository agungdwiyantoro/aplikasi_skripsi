package com.example.agung.PPK_UNY_Mobile;

import android.content.Context;

import com.google.firebase.storage.StorageMetadata;

import java.lang.ref.WeakReference;

class metaData {
    private String fileNameOri, fullName, phoneNumber, email;
    private WeakReference<Context> context;

    metaData(Context context, String fileNameOri, String fullName, String phoneNumber, String email) {
        this.context = new WeakReference<>(context);
        this.fileNameOri = fileNameOri;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    StorageMetadata storageMetaData(){
        return new StorageMetadata.Builder()
                //.setContentType("application/pdf")
                .setContentEncoding("UTF-8")
                .setCustomMetadata(context.get().getString(R.string.fileNameOri), fileNameOri)
                .setCustomMetadata(context.get().getResources().getString(R.string.fullName), fullName)
                .setCustomMetadata(context.get().getResources().getString(R.string.phoneNumber), phoneNumber)
                .setCustomMetadata(context.get().getString(R.string.email), email)
                .build();
    }
}
