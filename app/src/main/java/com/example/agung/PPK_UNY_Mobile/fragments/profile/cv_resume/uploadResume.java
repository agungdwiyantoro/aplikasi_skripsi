package com.example.agung.PPK_UNY_Mobile.fragments.profile.cv_resume;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.agung.PPK_UNY_Mobile.R;
import com.example.agung.PPK_UNY_Mobile.dropbox.FilesActivity;
import com.example.agung.PPK_UNY_Mobile.onedrivesdk.picker.IPickerResult;
import com.example.agung.PPK_UNY_Mobile.onedrivesdk.picker.LinkType;
import com.example.agung.PPK_UNY_Mobile.onedrivesdk.picker.Picker;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.example.agung.PPK_UNY_Mobile.utils.utils.filesize_in_kiloBytes;
import static com.example.agung.PPK_UNY_Mobile.utils.utils.timeMilisToDate;

public class uploadResume extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "uploadResume";

    private final int PERMISSION_REQUEST_CODE_DROPBOX = 10;
    private final int DROPBOX_CODE = 11;
    private final int PERMISSION_REQUEST_CODE_ONEDRIVE = 61680;
    private final int PERMISSION_REQUEST_CODE_LOCALFILE = 12;
    private int ONE_DRIVE_REQUEST_CODE = 0xF0F0;
    private TextView namaFile;
    private Button uploadFile;
    private Picker mPicker;
    private static String mNamaFile, filePath;
    private ConstraintLayout constraintLayout;


  //private final List<String> scopeList = Collections.singletonList(DriveScopes.DRIVE_FILE);
    private static final int REQUEST_CODE_SIGN_IN = 1;
    private static final int REQUEST_CODE_OPEN_DOCUMENT = 2;
  //private DriveServiceHelper mDriveServiceHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_resume);
        init();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_upload:
                sentData(mNamaFile, filePath);
                break;

            case R.id.bt_dropbox:
                Class x = FilesActivity.class;
                checkPermissionsAndOpenFilePicker(() -> {  browseDocument(x);}, PERMISSION_REQUEST_CODE_DROPBOX);
                break;

            case R.id.bt_google_drive:
                //requestSignIn();
                break;

            case R.id.bt_one_drive:
                /*
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    mPicker.startPicking(uploadResume.this, LinkType.DownloadLink);
                } else {
                    String[] permissionRequest = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permissionRequest, PERMISSION_REQUEST_CODE);
                }
                */
                checkPermissionsAndOpenFilePicker(() -> { mPicker.startPicking(uploadResume.this, LinkType.DownloadLink);}, PERMISSION_REQUEST_CODE_ONEDRIVE);
                break;

            case R.id.bt_browse_local_file:
               // showFileChooser();
                checkPermissionsAndOpenFilePicker(this::materialFilePicker, PERMISSION_REQUEST_CODE_LOCALFILE);
                break;

            case R.id.back_button:
                finish();
                break;
        }
    }

    void init() {
        namaFile = findViewById(R.id.tv_nama_file);
        uploadFile = findViewById(R.id.bt_upload);
        constraintLayout = findViewById(R.id.constraint_layout);
        Button dropbox = findViewById(R.id.bt_dropbox);
        Button googleDrive = findViewById(R.id.bt_google_drive);
        Button oneDrive = findViewById(R.id.bt_one_drive);
        Button localFIle = findViewById(R.id.bt_browse_local_file);
        ImageView backButton = findViewById(R.id.back_button);
        TextView tvToolbar = findViewById(R.id.tv_toolbar);

        tvToolbar.setText(getString(R.string.upload_resume));

        uploadFile.setEnabled(false);

        uploadFile.setOnClickListener(this);
        dropbox.setOnClickListener(this);
        googleDrive.setOnClickListener(this);
        oneDrive.setOnClickListener(this);
        localFIle.setOnClickListener(this);
        backButton.setOnClickListener(this);


        String ONEDRIVE_APP_ID = "548861bd-c757-41bb-baf3-f6fe1854293b";
        mPicker = (Picker) Picker.createPicker(ONEDRIVE_APP_ID);

    }

    private void browseDocument(Class x) {
        Intent intent = new Intent(uploadResume.this, x);
        startActivityForResult(intent , DROPBOX_CODE);
       // finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){

            case PERMISSION_REQUEST_CODE_DROPBOX :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Class x = FilesActivity.class;
                    browseDocument(x);
                } else {
                    //showSnackbar("YOU HAVE TO GRANTED THE PERMISSION");
                }
                break;

            case PERMISSION_REQUEST_CODE_ONEDRIVE :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    mPicker.startPicking(uploadResume.this, LinkType.DownloadLink);
                } else {
                    //showSnackbar("YOU HAVE TO GRANTED THE PERMISSION");
                }
                break;

            case PERMISSION_REQUEST_CODE_LOCALFILE :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    materialFilePicker();
                } else {
                    //showSnackbar("YOU HAVE TO GRANTED THE PERMISSION");
                }
                break;
        }

    }


    private File fileDirectory() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }


    private String save_directory(String file_name){
        Uri contentUril = FileProvider.getUriForFile(uploadResume.this, "com.example.agung.aplikasi_skripsi.provider", fileDirectory());
        return "sdcard/" + contentUril.toString() + "/" + file_name;
    }

    private void download_lowongan_details(String namaLowongan, String linkDownload) {
        Uri contentUril = FileProvider.getUriForFile(uploadResume.this, "com.example.agung.aplikasi_skripsi.provider", fileDirectory());
        Uri uri = Uri.parse(linkDownload);
        Log.d(TAG, "absolute path " + contentUril.getPath());
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDestinationInExternalPublicDir(contentUril.toString(), namaLowongan);
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setAllowedOverRoaming(false);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        downloadManager.enqueue(request);
    }


    private void sentData(String namaFile, String filePath) {
       // Bundle bundle = new Bundle();
        Intent intent = new Intent();
        intent.putExtra("FILE_NAME", namaFile);
        intent.putExtra("FILE_PATH", filePath);
        intent.putExtra("FILE_DATE", timeMilisToDate(new Date()));

        //intent.putExtras(bundle);
        //startActivity(intent);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //Toast.makeText(uploadResume.this, "requestCode "+ requestCode , Toast.LENGTH_LONG).show();

        switch(requestCode){

            case PERMISSION_REQUEST_CODE_LOCALFILE:

                if(resultCode==RESULT_OK){

                        filePath =  data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);


               //     String format = filePath.substring(filePath.lastIndexOf(".") + 1);


                        mNamaFile = filePath.substring(filePath.lastIndexOf("/") + 1);
                        File file = new File(Objects.requireNonNull(filePath));
                        String fileExt = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());


                        int fileSize = filesize_in_kiloBytes((int) file.length());


                        checkFile(mNamaFile, fileSize, fileExt);


                }
                break;


            case PERMISSION_REQUEST_CODE_ONEDRIVE:

                if(resultCode==RESULT_OK) {
                    IPickerResult result = mPicker.getPickerResult(requestCode, resultCode, data);

                    if (result != null) {

                        String filename = result.getName();
                        String linkDownload = result.getLink().toString();
                        filePath = save_directory(filename);
                        mNamaFile = filename;
                        int fileSize = (int) result.getSize();

                        File file = new File(Objects.requireNonNull(filename));
                        Uri fileUri = Uri.fromFile(file);
                        String fileExt = MimeTypeMap.getFileExtensionFromUrl(fileUri.toString());

                        if (checkFile(filename, filesize_in_kiloBytes(fileSize), fileExt)) {
                            uploadFile.setEnabled(true);
                            download_lowongan_details(filename, linkDownload);
                        }
                    }
                }

                break;



            case DROPBOX_CODE:
                Log.d(TAG, "ff ");
                if(resultCode == RESULT_OK && data!=null){
                    filePath = data.getStringExtra("FILE_PATH");
                    mNamaFile = data.getStringExtra("FILE_NAME");
                    int fileSize = data.getIntExtra("FILE_SIZE", 0);

                    File file = new File(Objects.requireNonNull(filePath));
                    String fileExt = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());

                    Log.d(TAG, "path " + filePath);
                    Log.d(TAG, "name" + mNamaFile);
                    Log.d(TAG, "size " + fileSize);
                    Log.d(TAG, "exe " + fileExt);
                    //checkFile(mNamaFile, fileSize, fileExt);

                    //fileUploaded();

                    //fileName.setText(dataFileName);
                    //fileDate.setText(timeMilisToDate(new Date()));

                    checkFile(mNamaFile, fileSize, fileExt);
                }
                break;
        }



     super.onActivityResult(requestCode, resultCode, data);
    }

    private void materialFilePicker(){
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(PERMISSION_REQUEST_CODE_LOCALFILE)
                .withFilter(Pattern.compile(".*\\.pdf$")) // Filtering files and directories by file name using regexp
              //  .withFilterDirectories(true) // Set directories filterable (false by default)
                .withHiddenFiles(true) // Show hidden files and folders
                .withTitle("Pick your file (.pdf)")
             //   .withPath(getFilesDir().getAbsolutePath())
                .start();
    }



    @Override
    protected void onResume() {
        super.onResume();
        Bundle b = getIntent().getExtras();

        String fileExt;

        if (b != null) {
            mNamaFile = b.getString("FILE_NAME");
            filePath = b.getString("FILE_PATH");
            int fileSize = b.getInt("FILE_SIZE");

            File file = new File(Objects.requireNonNull(b.getString("FILE_PATH")));
            Uri fileUri = Uri.fromFile(file);
            fileExt = MimeTypeMap.getFileExtensionFromUrl(fileUri.toString());

            Log.d("uploadResume", filePath);

            checkFile(mNamaFile, fileSize, fileExt);
        }
    }

    private void checkPermissionsAndOpenFilePicker(Runnable runnable, int REQUEST_CODE) {
        String permissionReadExternalStorage = Manifest.permission.READ_EXTERNAL_STORAGE;
        String permissionWriteExternalStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE;

       // ActivityCompat.requestPermissions(this, new String[]{permissionReadExternalStorage, permissionWriteExternalStorage}, REQUEST_CODE);

        if (    ContextCompat.checkSelfPermission(this, permissionReadExternalStorage) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, permissionWriteExternalStorage) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{permissionReadExternalStorage, permissionWriteExternalStorage}, REQUEST_CODE);
        }

        else {
            runnable.run();
            //materialFilePicker();
        }
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
       // intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"), 1);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkFile(String mNamaFile, int fileSize, String fileExt){
        boolean x = false;
        if (fileSize < 5000 && fileExt.equals("pdf")) {
            namaFile.setText(mNamaFile);
            uploadFile.setEnabled(true);
            namaFile.setBackgroundColor(getResources().getColor(R.color.gray, null));
            x = true;
        } else if (!fileExt.equals("pdf")) {
            uploadFile.setEnabled(false);
            namaFile.setBackgroundColor(getResources().getColor(R.color.red, null));
            namaFile.setText(getResources().getString(R.string.mustPdfFile));
            x = false;
        } else if (fileSize > 5000) {
            uploadFile.setEnabled(false);
            namaFile.setBackgroundColor(getResources().getColor(R.color.red, null));
            namaFile.setText(getResources().getString(R.string.fileTooLarge));
            x = false;
        }
        return x;
    }
}






/*
GOOGLE DRIVE

  private void requestSignIn() {
        Log.d(TAG, "Requesting sign-in");

        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                        .build();
        GoogleSignInClient client = GoogleSignIn.getClient(this, signInOptions);

        // The result of the sign-in Intent is handled in onActivityResult.
        startActivityForResult(client.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }

    private void handleSignInResult(Intent result) {
        GoogleSignIn.getSignedInAccountFromIntent(result)
                .addOnSuccessListener(googleAccount -> {
                    Log.d(TAG, "Signed in as " + googleAccount.getEmail());

                    // Use the authenticated account to sign in to the Drive service.
                    GoogleAccountCredential credential =
                            GoogleAccountCredential.usingOAuth2(
                                    this, Collections.singleton(DriveScopes.DRIVE_FILE));
                    credential.setSelectedAccount(googleAccount.getAccount());
                    Drive googleDriveService =
                            new Drive.Builder(
                                    AndroidHttp.newCompatibleTransport(),
                                    new GsonFactory(),
                                    credential)
                                    .setApplicationName("Drive API Migration")
                                    .build();

                    // The DriveServiceHelper encapsulates all REST API and SAF functionality.
                    // Its instantiation is required before handling any onClick actions.
                    mDriveServiceHelper = new DriveServiceHelper(googleDriveService);
                    openFilePicker();
                })
                .addOnFailureListener(exception -> Log.e(TAG, "Unable to sign in.", exception));
    }

    private void openFilePicker() {
        if (mDriveServiceHelper != null) {
            Log.d(TAG, "Opening file picker.");

            Intent pickerIntent = mDriveServiceHelper.createFilePickerIntent();

            // The result of the SAF Intent is handled in onActivityResult.
            startActivityForResult(pickerIntent, REQUEST_CODE_OPEN_DOCUMENT);
        }
    }

    private void openFileFromFilePicker(Uri uri) {
        if (mDriveServiceHelper != null) {
            Log.d(TAG, "Opening " + uri.getPath());

            mDriveServiceHelper.openFileUsingStorageAccessFramework(getContentResolver(), uri)
                    .addOnSuccessListener(nameAndContent -> {
                        String name = nameAndContent.first;
                        String content = nameAndContent.second;

                       // mFileTitleEditText.setText(name);
                       // mDocContentEditText.setText(content);

                        // Files opened through SAF cannot be modified.
                       // setReadOnlyMode();
                    })
                    .addOnFailureListener(exception ->
                            Log.e(TAG, "Unable to open file from picker.", exception));
        }
    }



    private void createFile() {
        if (mDriveServiceHelper != null) {
            Log.d(TAG, "Creating a file.");

            mDriveServiceHelper.createFile()
                    .addOnSuccessListener(fileId -> readFile(fileId))
                    .addOnFailureListener(exception ->
                            Log.e(TAG, "Couldn't create file.", exception));
        }
    }

    private void readFile(String fileId) {
        if (mDriveServiceHelper != null) {
            Log.d(TAG, "Reading file " + fileId);

            mDriveServiceHelper.readFile(fileId)
                    .addOnSuccessListener(nameAndContent -> {
                        String name = nameAndContent.first;
                        String content = nameAndContent.second;

                       // mFileTitleEditText.setText(name);
                        //mDocContentEditText.setText(content);

                        //setReadWriteMode(fileId);
                    })
                    .addOnFailureListener(exception ->
                            Log.e(TAG, "Couldn't read file.", exception));
        }
    }

    private void query() {
        if (mDriveServiceHelper != null) {
            Log.d(TAG, "Querying for files.");

            mDriveServiceHelper.queryFiles()
                    .addOnSuccessListener(fileList -> {
                        StringBuilder builder = new StringBuilder();
                        for (com.google.api.services.drive.model.File file : fileList.getFiles()) {
                            builder.append(file.getName()).append("\n");
                        }
                        String fileNames = builder.toString();

                       // mFileTitleEditText.setText("File List");
                       // mDocContentEditText.setText(fileNames);

                       // setReadOnlyMode();
                    })
                    .addOnFailureListener(exception -> Log.e(TAG, "Unable to query files.", exception));
        }
    }


    private void setReadOnlyMode() {
       // mFileTitleEditText.setEnabled(false);
       // mDocContentEditText.setEnabled(false);
      //  mOpenFileId = null;
    }


      Updates the UI to read/write mode on the document identified by {@code fileId}.

private void setReadWriteMode(String fileId) {
     mFileTitleEditText.setEnabled(true);
      mDocContentEditText.setEnabled(true);
     mOpenFileId = fileId;
}


 */
