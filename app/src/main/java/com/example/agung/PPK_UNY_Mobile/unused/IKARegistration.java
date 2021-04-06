package com.example.agung.PPK_UNY_Mobile.unused;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.example.agung.PPK_UNY_Mobile.BaseActivity;
import com.example.agung.PPK_UNY_Mobile.R;
import com.example.agung.PPK_UNY_Mobile.model.model_IKA_member;
import com.example.agung.PPK_UNY_Mobile.uploadDiploma;
import com.example.agung.PPK_UNY_Mobile.utils.utils;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class IKARegistration extends BaseActivity implements View.OnClickListener {
    private Button save;
    private Button x;
    private EditText fullname, studentID, phoneNumber;
    private TextView file_name, tv_toolbar, inProcess, fileDate;
    private static String str_filePath, str_fileName, str_fileDate;
    private ConstraintLayout constraintLayout;
    private Button sign_up;

    //private ImageView iv_document;

    private FirebaseStorage fs = FirebaseStorage.getInstance();

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    private CollectionReference IKA_member = firebaseFirestore.
            collection("IKA_member_registration");


    private String path;
    FirebaseStorage storage;
    StorageReference storageRef;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ika_registration);
        init();

        path = "diploma/" + getEmail() + "/" + "diploma(" + getEmail() + ")";

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child(path);


        checkAnggotaIKAregistration();

        tv_toolbar.setText(getString(R.string.registrasi_IKA));


    }

    private void init(){
        sign_up = findViewById(R.id.bt_daftar);
        fullname = findViewById(R.id.et_nama_lengkap);
        studentID = findViewById(R.id.et_studentID);
        phoneNumber = findViewById(R.id.et_phone_number);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        ImageView back_button = findViewById(R.id.back_button);
        file_name = findViewById(R.id.tv_file_name);
        fileDate = findViewById(R.id.tv_file_date);
        x = findViewById(R.id.bt_x);
        constraintLayout = findViewById(R.id.constraintlayout);
        inProcess = findViewById(R.id.tv_waiting_verification);
        save = findViewById(R.id.bt_apply);
       // iv_document = findViewById(R.id.iv_document);

        save.setOnClickListener(this);
        back_button.setOnClickListener(this);
        sign_up.setOnClickListener(this);
        x.setOnClickListener(this);
       // iv_document.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_daftar :
                if(!(fullname.getText().toString().isEmpty() ||
                        studentID.getText().toString().isEmpty() ||
                        phoneNumber.getText().toString().isEmpty() ||
                        str_filePath == null))
            {
                /*
                metaDataDiploma md = new metaDataDiploma(IKARegistration.this,
                        fullname.getText().toString(),
                        studentID.getText().toString(),
                        phoneNumber.getText().toString(),
                        str_fileName);
                 */

                //new firebaseUploadFiles(IKARegistration.this, new File(str_filePath), path, md.storageMetaData(), constraintLayout).execute();


                model_IKA_member mIKA = new model_IKA_member(fullname.getText().toString(),
                        studentID.getText().toString(),
                        phoneNumber.getText().toString(),
                        path, utils.timeMilisToDate_appliedJobs(str_fileDate));

                IKA_member.document(studentID.getText().toString()).set(mIKA);


            }

            else{
                showSnackbar(constraintLayout, getString(R.string.all_field_must_be_filled));
            }
                break;

            case R.id.back_button :
                this.finish();
                break;

            case R.id.bt_apply :
              //  metaDataDiploma md = new metaDataDiploma(IKARegistration.this, phoneNumber.getText().toString());
                showSnackbar(constraintLayout, getString(R.string.profileUpdated));
               // new updateMetaData(fs.getReference().child(path), md.storageMetaDataPhoneNumber()).execute();
                break;

            case R.id.bt_x :
                alertDialogDeleteResume();
                break;
/*
            case R.id.iv_document :
                alertDialogs.bannerFullScreenLocal(IKARegistration.this, str_filePath);
                break;

 */
        }
    }



    private void checkAnggotaIKAregistration(){
        BaseActivity.metaDataDiploma(mIKAmember -> {
            if(mIKAmember != null) {
                checkAnggotaIKAmember(mIKAmember.getNIM());
                fullname.setText(mIKAmember.getNama_lengkap());
                studentID.setText(mIKAmember.getNIM());
                phoneNumber.setText(mIKAmember.getPhoneNumber());
                file_name.setText(mIKAmember.getFileName());
                fileDate.setText(utils.timeMilisToDate(mIKAmember.getFileDate()));

                fullname.setEnabled(false);
                studentID.setEnabled(false);

                fullname.setTextColor(getColor(R.color.white));
                studentID.setTextColor(getColor(R.color.white));

                x.setVisibility(View.GONE);
                sign_up.setVisibility(View.GONE);
                inProcess.setVisibility(View.VISIBLE);
                save.setVisibility(View.VISIBLE);
            }
            else{
                fileChosen();

            }
        }, IKARegistration.this, constraintLayout, path);


    }

    private void checkAnggotaIKAmember(String studentID){
        firebaseFirestore.collection("IKA_member")
                .document(studentID)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        if(task.getResult().getString("nim")!=null) {
                            inProcess.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.wrapper_verified, null));
                            inProcess.setText(getString(R.string.verified));
                            x.setVisibility(View.GONE);
                            FirebaseMessaging.getInstance().subscribeToTopic("IKA_UNY");
                        }
                        //setPhotoDiploma();
                    }

                });
    }

    private void fileChosen() {

        Bundle data = getIntent().getExtras();
        if(data!=null) {
            str_fileName = data.getString("FILE_NAME");
            str_fileDate = data.getString("FILE_DATE");
            str_filePath = data.getString("FILE_PATH");

            file_name.setText(str_fileName);
            fileDate.setText(str_fileDate);


            Log.d("FILE PATH", data.getString("FILE_PATH"));
            Log.d("FILE DATE", data.getString("FILE_DATE"));
            Log.d("FILE NAME", data.getString("FILE_NAME"));
        }
    }
    /*
    private void setPhotoDiploma() {
        StorageReference diploma = fs.getReference().child(path);
        diploma.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get()
                    .load(uri)
                    .into(iv_diploma);

            Log.d("IKARegistration Success", uri.toString());
        }).addOnFailureListener(e -> Log.d("IKARegistration Failed", e.toString()));

    }
    */

    private void deleteFileResume(){
        storageRef.delete().addOnCompleteListener(task -> {
           /*
           Intent intent = new Intent(resumeProfile.this, uploadResume.class);
           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           startActivity(intent);
           finish();
            */

            toActivity(IKARegistration.this, uploadDiploma.class);
            finish();

        }).addOnFailureListener(e -> showSnackbar(constraintLayout, getString(R.string.errorOccured)));
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

        yes.setOnClickListener(view -> deleteFileResume());
        no.setOnClickListener(view -> dialog.dismiss());
        x.setOnClickListener(view -> dialog.dismiss());
    }


}
