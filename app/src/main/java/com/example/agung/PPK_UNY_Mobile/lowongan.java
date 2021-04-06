package com.example.agung.PPK_UNY_Mobile;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;

import static com.google.android.material.snackbar.Snackbar.LENGTH_LONG;

public class lowongan extends BaseActivity{

    private int STORAGE_PERMISSION_CODE = 1234;
    private PDFView pdfView;
    private String namaFile, linkDownload;
    private static boolean networkStatus;
    private FrameLayout frameLayout;
    private static long status;
    private DownloadManager downloadManager;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        pdfView = findViewById(R.id.pdfView);
        frameLayout = findViewById(R.id.frame_layout);
        namaFile = getIntent().getStringExtra("namaLowongan");
        linkDownload = getIntent().getStringExtra("linkDownload");

        mProgress = new ProgressDialog(this);
        mProgress.setMessage(getString(R.string.loading));
        mProgress.create();
        mProgress.show();



        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        checkPermissionsAndOpenFilePicker(STORAGE_PERMISSION_CODE);
/*
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            file_check_exist();
        } else {
            String[] permissionRequest = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissionRequest, STORAGE_PERMISSION_CODE);
        }

 */
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode ==STORAGE_PERMISSION_CODE) {
            // we have heard back from our request for camera and write external storage.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                file_check_exist();
            } else {
                Snackbar.make(frameLayout, getString(R.string.grantPermissions), LENGTH_LONG).show();
            }
        }
    }

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
                mProgress.dismiss();
                pdfView.fromFile(new File(save_directory(namaFile))).load();
        }
    };

    private void download_lowongan_details(String namaLowongan, String linkDownload){
        Log.d("lowongan", "FUCK SHIT");
        //Uri contentUril = FileProvider.getUriForFile(lowongan.this, "com.example.agung.aplikasi_skripsi.provider", fileDirectory());
        Uri uri = Uri.parse(linkDownload);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOCUMENTS, namaLowongan + ".pdf");
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        request.setAllowedOverRoaming(false);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        downloadManager.enqueue(request);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onComplete);
    }

    private File fileDirectory(){
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
    }

    private String save_directory(String file_name){
//        Uri contentUril = FileProvider.getUriForFile(lowongan.this, "com.example.agung.aplikasi_skripsi.provider", fileDirectory());
        return "/storage/emulated/0/Android/data/com.example.agung.PPK_UNY_Mobile/files/" + Environment.DIRECTORY_DOCUMENTS + "/" +  file_name + ".pdf";
    }

    private void file_check_exist(){
        File file_exist = new File(save_directory(namaFile));
        Log.d("TESTTT", save_directory(namaFile));
        if(file_exist.exists()){
            mProgress.dismiss();
            pdfView.fromFile(file_exist).load();
        }
        else{
            if (networkStatus){

                if(!linkDownload.isEmpty()) {
                    download_lowongan_details(namaFile, linkDownload);
                }
                else{
                    mProgress.dismiss();
                    showSnackbarAddCallback(frameLayout, getString(R.string.errorOccured), lowongan.this);
                }
            }
            else{
                mProgress.dismiss();
                showSnackbarAddCallback(frameLayout, getString(R.string.no_internet), lowongan.this);
            }
        }
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        super.onInternetConnectivityChanged(isConnected);
        networkStatus = isConnected;
    }

    private void checkPermissionsAndOpenFilePicker(int REQUEST_CODE) {
        String permissionReadExternalStorage = Manifest.permission.READ_EXTERNAL_STORAGE;
        String permissionWriteExternalStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE;

        if (    ContextCompat.checkSelfPermission(this, permissionReadExternalStorage) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, permissionWriteExternalStorage) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permissionReadExternalStorage, permissionWriteExternalStorage}, REQUEST_CODE);
        }

        else {
            file_check_exist();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == STORAGE_PERMISSION_CODE) {

            if (resultCode == RESULT_OK) {
                Snackbar.make(frameLayout, getString(R.string.grantPermissions), LENGTH_LONG).show();
            }
        }
    }
}






