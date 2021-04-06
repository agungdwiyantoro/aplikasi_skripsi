package com.example.agung.PPK_UNY_Mobile;

import android.content.Context;

import com.google.firebase.storage.StorageMetadata;

import java.lang.ref.WeakReference;

public class metaDataNew {
    private String fileNameOri, date;
    private WeakReference<Context> context;

    public metaDataNew(Context context, String fileNameOri, String date) {
        this.context = new WeakReference<>(context);
        this.fileNameOri = fileNameOri;
        this.date = date;
    }

    public StorageMetadata storageMetaData(){
        return new StorageMetadata.Builder()
                //.setContentType("application/pdf")
                .setContentEncoding("UTF-8")
                .setCustomMetadata(context.get().getString(R.string.fileNameOri), fileNameOri)
                .setCustomMetadata(context.get().getResources().getString(R.string.birth_date), date)
                .build();
    }
}
