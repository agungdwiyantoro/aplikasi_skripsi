package com.example.agung.PPK_UNY_Mobile.firebase;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;

import com.example.agung.PPK_UNY_Mobile.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.lang.ref.WeakReference;

public class firebaseUploadFiles extends AsyncTask<Void, Void, Void> {
    private final String TAG = "firebaseUploadFiles";

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference();

    private String path;;
    private File file;
    private StorageMetadata storageMetadata;
    private WeakReference<Context> context;
    private ProgressDialog mProgressDialog;
    private WeakReference<View> view;

    public firebaseUploadFiles(Context context, File file, String path, StorageMetadata storageMetadata,View view){
        this.file = file;
        this.path = path;
        this.storageMetadata = storageMetadata;
        this.context = new WeakReference<>(context);
        this.view = new WeakReference<>(view);
    }

    @Override
    protected Void doInBackground(Void... strings) {
        uploadFile();
        return null;
    }

    private void uploadFile(){
        Uri uri = Uri.fromFile(file);
        StorageReference documentRef = storageReference.child(path);
        UploadTask uploadTask = documentRef.putFile(uri, storageMetadata);

        uploadTask.addOnSuccessListener(taskSnapshot -> {

            hideProgressDialog();
            showSnackbar(view.get(), context.get().getString(R.string.fileIsUploaded));
        });

        uploadTask.addOnFailureListener(e -> {
            hideProgressDialog();
            showSnackbar(view.get(),context.get().getString(R.string.failedUploading));});

        uploadTask.addOnProgressListener(taskSnapshot -> {
            showProgressDialog(context.get());
        });

    }

    private void showProgressDialog(Context context){


        if(mProgressDialog==null){
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();

    }

    private void hideProgressDialog(){
        if(mProgressDialog !=null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }

    private void showSnackbar(View view, String text){
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    };


}
