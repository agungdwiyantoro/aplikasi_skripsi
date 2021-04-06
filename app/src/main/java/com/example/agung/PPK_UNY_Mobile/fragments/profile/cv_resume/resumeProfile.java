package com.example.agung.PPK_UNY_Mobile.fragments.profile.cv_resume;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.example.agung.PPK_UNY_Mobile.BaseActivity;
import com.example.agung.PPK_UNY_Mobile.R;
import com.example.agung.PPK_UNY_Mobile.firebase.firebaseUploadFiles;
import com.example.agung.PPK_UNY_Mobile.metaDataNew;
import com.example.agung.PPK_UNY_Mobile.model.model_user;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.agung.PPK_UNY_Mobile.utils.utils.timeMilisToDate;

public class resumeProfile extends BaseActivity implements View.OnClickListener, TextWatcher {


    private final String TAG = "resumeProfile";
    public static final int REQUEST_CODE = 1;

    private EditText fullName;
    private EditText phoneNumber;
    private EditText birthPlace;

    private EditText birthDate;
    private TextView datePicked;
    private RadioButton male, female;

    private String mNamaFile, mFilePath, mFileDate;
    private File file;

    private LinearLayout constraintLayout;

    private ImageView iv_document;
    private Button bt_x;
    private TextView fileDate;
    private TextView fileName;

    private Button choose;

    FirebaseStorage storage;
    StorageReference storageRef;


    private String fileNameOriSt = null;

    private String path;

    private static String dataFilePath, dataFileName;

    private SimpleDateFormat dateFormatter;

    //private TextView preview;
    private DatePickerDialog datePickerDialog;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DocumentReference users = firebaseFirestore.
            collection("users").
            document(getEmail());


    private String current = "";
    private final Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resume_profile);
        init();
    }

    private void init(){
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        path = "resumes/" + getEmail() + "/" + "resume(" +  getEmail() + ")";
        fullName = findViewById(R.id.et_full_name);
        phoneNumber = findViewById(R.id.et_phone_number);
        EditText email = findViewById(R.id.et_email);
        TextView tv_toolbar =findViewById(R.id.tv_toolbar);
        ImageView backButton = findViewById(R.id.back_button);
        birthDate = findViewById(R.id.et_birth_date);
        birthPlace = findViewById(R.id.et_birth_place);
        datePicked = findViewById(R.id.tv_date);
        //preview = findViewById(R.id.tv_preview);
        male = findViewById(R.id.laki);
        female = findViewById(R.id.perempuan);
        Button save = findViewById(R.id.bt_save);
        bt_x = findViewById(R.id.bt_x);
        fileName = findViewById(R.id.tv_file_name);
        fileDate = findViewById(R.id.tv_file_date);
        iv_document = findViewById(R.id.iv_document);
        choose = findViewById(R.id.bt_upload_files);

        fileName.setSelected(true);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child(path);

        constraintLayout = findViewById(R.id.constraint_layout);
        tv_toolbar.setText(getString(R.string.your_profile));

        /*
        Bundle b = getIntent().getExtras();

        if(b!=null) {
            mNamaFile = b.getString("FILE_NAME");
            mFilePath = b.getString("FILE_PATH");
            mFileDate = b.getString("FILE_DATE");

            if (mFilePath != null) {
                file = new File(mFilePath);
            }
        }
*/
        BaseActivity.metaData(model_file_meta_data -> {
            if(model_file_meta_data!=null){
                fileUploaded();
                fileName.setText(model_file_meta_data.getFileName());
                fileDate.setText(timeMilisToDate(model_file_meta_data.getDate()));
            }
            else{
                //fileName.setText(mNamaFile);
                //fileDate.setText(mFileDate);
                choose.setVisibility(View.VISIBLE);
                bt_x.setVisibility(View.INVISIBLE);
            }
        }, resumeProfile.this, constraintLayout, path);


        users.get().addOnCompleteListener(task -> {
            if(task.getResult().getString("name")!=null ||
                    task.getResult().getString("birth_date")!=null ||
                    task.getResult().getString("birth_place")!=null ||
                    task.getResult().getString("handphone")!=null ||
                    task.getResult().getBoolean("jenisKelamin")!=null){

                fullName.setText(task.getResult().getString("name"));
                birthDate.setText(task.getResult().getString("birth_date"));
                birthPlace.setText(task.getResult().getString("birth_place"));
                phoneNumber.setText(task.getResult().getString("handphone"));

                if(task.getResult().getBoolean("jenisKelamin").toString().equals("true")){
                    male.setChecked(true);
                }
                else{
                    female.setChecked(true);
                }
            }
        });

        email.setText(getEmail());

        save.setOnClickListener(this);
        bt_x.setOnClickListener(this);
        backButton.setOnClickListener(this);
        birthDate.addTextChangedListener(this);
        choose.setOnClickListener(this);
        //preview.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        metaDataNew md;
        switch (view.getId()){
            case R.id.bt_save :

                String sFullName = fullName.getText().toString();
                String sBirthDate = birthDate.getText().toString();
                String sBirthPlace = birthPlace.getText().toString();
                String sPhoneNumber = phoneNumber.getText().toString();
                boolean sex = false;

                if(!(sFullName.isEmpty()||sBirthPlace.isEmpty()||sPhoneNumber.isEmpty()||sBirthDate.isEmpty())) {
                    if (male.isChecked()) {
                        sex = true;
                    } else if (female.isChecked()) {
                        sex = false;
                    }
                    model_user m_user = new model_user(sFullName, sBirthPlace, sBirthDate, getEmail(), sPhoneNumber, sex);
                    users.set(m_user);

                    if (dataFilePath != null) {
                        md = new metaDataNew(resumeProfile.this, dataFileName, timeMilisToDate(new Date()));
                        new firebaseUploadFiles(resumeProfile.this, new File(dataFilePath), path, md.storageMetaData(), constraintLayout).execute();
                    } else {
                        //md = new metaDataN(resumeProfile.this, fileNameOriSt, sFullName, sPhoneNumber, FirebaseAuth.getInstance().getCurrentUser().getEmail());
                        showSnackbar(constraintLayout, getString(R.string.profileUpdated));
                        //new updateMetaData(storageRef, md.storageMetaData()).execute();
                    }
                }
                else{
                    BaseActivity.showSnackbarstatic(constraintLayout, getString(R.string.all_field_must_be_filled));
                }

                break;

            case R.id.bt_x :
                alertDialogDeleteResume();
                break;

            case R.id.bt_quick_apply :
                //new SendMailTask(resumeProfile.this, constraintLayout).execute("ppkunymobile@gmail.com", "basch235re5", FirebaseAuth.getInstance().getCurrentUser().getEmail(), "TESTING PROGRAM", "If an app or site doesn’t meet our security standards, Google might block anyone who’s trying to sign in to your account from it. Less secure apps can make it easier for hackers to get in to your account, so blocking sign-ins from these apps helps keep your account safe.");
           /*
                try {
                    GmailService gmailService = new GmailServiceImpl(GoogleNetHttpTransport.newTrustedTransport());
                    gmailService.setGmailCredentials(GmailCredentials.builder()
                            .userEmail("YOUR_EMAIL@gmail.com")
                            .clientId("1078329436949-rspqf1hkbedrqavvcakn8k6l6qd9asnl.apps.googleusercontent.com")
                            .clientSecret("7Axm_PMdL3lppLdkA-MmPo7h")
                            .accessToken("ya29.GluCBY6YE-TzEU2-F86sRl_Gq5QyPmUNW2wEV0MynFN-L3HK2AHEUD09pknXfrvk8UY6NYnGwuCIxAh97s6ipVylgwoNIsxLs7uouIBqj8vWiAODGiS2a1ZDNa8D")
                            .refreshToken("1/XyMnZb4UfU8WDt6SnHIeE3wFTPyTAg2K16ZA7NIF0bY")
                            .build());

                    gmailService.sendMessage("RECIPIENT_EMAIL@gmail.com", "Subject", "body text");
                } catch (GeneralSecurityException | IOException | MessagingException e) {
                    e.printStackTrace();
                }

              */
                break;

            case R.id.back_button :
                finish();
                break;

            case R.id.bt_upload_files :
                //toActivity();

                Intent intent = new Intent(resumeProfile.this,
                        uploadResume.class);
                startActivityForResult(intent , REQUEST_CODE);
                break;
                /*
            case R.id.tv_preview :
                Log.d("resumeProfile save", save_directory(mNamaFile));
                Log.d("resumeProfile mNamaPath", mFilePath);
                alertDialogs.pdfPreviewLocal(resumeProfile.this, mFilePath);
                break;
                 */
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data!=null){
                dataFilePath = data.getStringExtra("FILE_PATH");
                dataFileName = data.getStringExtra("FILE_NAME");

                fileUploaded();

                fileName.setText(dataFileName);
                fileDate.setText(timeMilisToDate(new Date()));
            }

        }catch (Exception e){
            showSnackbar(constraintLayout, getString(R.string.errorOccured));
        }
    }

    private void deleteFileResume(AlertDialog dialog){

            storageRef.delete().addOnCompleteListener(task -> {
           /*
           Intent intent = new Intent(resumeProfile.this, uploadResume.class);
           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           startActivity(intent);
           finish();
            */
                //toActivity(resumeProfile.this, uploadResume.class);


                fileIsNotUploaded();

                //finish();

                dialog.dismiss();


            });//.addOnFailureListener(e -> showSnackbar(constraintLayout, getString(R.string.errorOccured)));

    }

    private void alertDialogDeleteResume(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = this.getLayoutInflater().inflate(R.layout.alert_delete_file, null);
        builder.setView(dialogView);
        builder.setCancelable(true);

        AlertDialog dialog = builder.show();

        Button x = dialogView.findViewById(R.id.bt_x);
        Button yes = dialogView.findViewById(R.id.bt_yes);
        Button no = dialogView.findViewById(R.id.bt_no);

        yes.setOnClickListener(view -> deleteFileResume(dialog));
        no.setOnClickListener(view -> dialog.dismiss());
        x.setOnClickListener(view -> dialog.dismiss());
    }

    private File fileDirectory(){
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
    }

    private String save_directory(String file_name){
        Uri contentUril = FileProvider.getUriForFile(resumeProfile.this, "com.example.agung.aplikasi_skripsi.provider", fileDirectory());
        return "sdcard/" + contentUril.toString() + "/" + file_name;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (!birthDate.getText().toString().equals(current)) {
            String clean = birthDate.getText().toString().replaceAll("[^\\d.]|\\.", "");
            String cleanC = current.replaceAll("[^\\d.]|\\.", "");

            int cl = clean.length();
            int sel = cl;
            for (int x = 2; x <= cl && x < 6; x += 2) {
                sel++;
            }
            //Fix for pressing delete next to a forward slash
            if (clean.equals(cleanC)) sel--;

            if (clean.length() < 8){
                String ddmmyyyy = "DDMMYYYY";
                clean = clean + ddmmyyyy.substring(clean.length());
            }else{
                //This part makes sure that when we finish entering numbers
                //the date is correct, fixing it otherwise
                int day  = Integer.parseInt(clean.substring(0,2));
                int mon  = Integer.parseInt(clean.substring(2,4));
                int year = Integer.parseInt(clean.substring(4,8));

                mon = mon < 1 ? 1 : Math.min(mon, 12);
                cal.set(Calendar.MONTH, mon-1);
                year = (year<1900)?1900: Math.min(year, 2100);
                cal.set(Calendar.YEAR, year);
                // ^ first set year for the line below to work correctly
                //with leap years - otherwise, date e.g. 29/02/2012
                //would be automatically corrected to 28/02/2012

                day = Math.min(day, cal.getActualMaximum(Calendar.DATE));
                clean = String.format(Locale.US, "%02d%02d%02d",day, mon, year);
            }

            clean = String.format("%s/%s/%s", clean.substring(0, 2),
                    clean.substring(2, 4),
                    clean.substring(4, 8));

            sel = Math.max(sel, 0);
            current = clean;
            birthDate.setText(current);
            birthDate.setSelection(Math.min(sel, current.length()));
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void fileUploaded(){
        choose.setVisibility(View.INVISIBLE);
        fileName.setVisibility(View.VISIBLE);
        fileDate.setVisibility(View.VISIBLE);
        iv_document.setVisibility(View.VISIBLE);
        bt_x.setVisibility(View.VISIBLE);
    }

    private void fileIsNotUploaded(){
        choose.setVisibility(View.VISIBLE);
        fileName.setVisibility(View.INVISIBLE);
        fileDate.setVisibility(View.INVISIBLE);
        iv_document.setVisibility(View.INVISIBLE);
        bt_x.setVisibility(View.INVISIBLE);
    }
}
