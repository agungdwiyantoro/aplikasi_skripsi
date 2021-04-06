package com.example.agung.PPK_UNY_Mobile.alertDialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.agung.PPK_UNY_Mobile.R;
import com.example.agung.PPK_UNY_Mobile.fragments.profile.cv_resume.resumeProfile;
import com.example.agung.PPK_UNY_Mobile.uploadDiploma;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Date;

import static com.example.agung.PPK_UNY_Mobile.BaseActivity.toActivity;
import static com.example.agung.PPK_UNY_Mobile.utils.utils.timeMilisToDate_with_hours;

public class alertDialogs {

    public static void DialogFormUploadResume( Activity a) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(a);
        View dialogView = a.getLayoutInflater().inflate(R.layout.alert_resume_uploads, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        Button upload = dialogView.findViewById(R.id.bt_upload);
        Button notNow = dialogView.findViewById(R.id.bt_not_now);
        Button x = dialogView.findViewById(R.id.bt_x);


        AlertDialog alertDialog = dialog.show();

        upload.setOnClickListener(view -> {

            toActivity(a, resumeProfile.class);
            alertDialog.dismiss();

        });

        notNow.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        x.setOnClickListener(view -> alertDialog.dismiss());
    }



    public static void DialogForm( Activity a, Runnable v, String file_name_s, Date file_date_s, String companyName) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(a);
        View dialogView = a.getLayoutInflater().inflate(R.layout.alert_job_dialog, null);
        TextView file_name = dialogView.findViewById(R.id.file_name);
        TextView file_date = dialogView.findViewById(R.id.file_date);
        TextView company_name = dialogView.findViewById(R.id.company_name_tv);
        Button submit = dialogView.findViewById(R.id.bt_submit);
        Button notNow = dialogView.findViewById(R.id.bt_not_now);
        Button x = dialogView.findViewById(R.id.bt_x);


        file_name.setText(file_name_s);
        file_date.setText(timeMilisToDate_with_hours(file_date_s));
        company_name.setText(companyName);

        dialog.setView(dialogView);
        dialog.setCancelable(true);

        AlertDialog alertDialog = dialog.show();

        submit.setOnClickListener(view -> {
            v.run();

            alertDialog.dismiss();

        });

        notNow.setOnClickListener(view -> {
            alertDialog.dismiss();
        });


        x.setOnClickListener(view -> alertDialog.dismiss());


    }


    public static void DialogFormGuess( Activity a) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(a);
        View dialogView = a.getLayoutInflater().inflate(R.layout.alert_job_guess_dialog, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        Button signUp = dialogView.findViewById(R.id.bt_sign_up);
        Button login = dialogView.findViewById(R.id.bt_login);
        Button x = dialogView.findViewById(R.id.bt_x);

        AlertDialog dialogInterface = dialog.show();
        signUp.setOnClickListener(view -> {
            toActivity(a, com.example.agung.PPK_UNY_Mobile.signUp.class);
            dialogInterface.dismiss();
        });

        login.setOnClickListener(view -> {
            toActivity(a, com.example.agung.PPK_UNY_Mobile.login.class);
            dialogInterface.dismiss();
        });

        x.setOnClickListener(view -> dialogInterface.dismiss());

    }

    public static void alert_tracer_study( Activity a) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(a);
        View dialogView = a.getLayoutInflater().inflate(R.layout.alert_job_tracer_study, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        Button signUp = dialogView.findViewById(R.id.bt_sign_up);
        Button cancel = dialogView.findViewById(R.id.bt_cancel);
        Button x = dialogView.findViewById(R.id.bt_x);

        AlertDialog dialogInterface = dialog.show();

        signUp.setOnClickListener(view -> {
            toActivity(a, uploadDiploma.class);
            dialogInterface.dismiss();
        });

        cancel.setOnClickListener(view -> {
            dialogInterface.dismiss();
        });

        x.setOnClickListener(view -> dialogInterface.dismiss());

    }

    public static void resetPassword(Activity a, ConstraintLayout constraintLayout){
        AlertDialog.Builder dialog = new AlertDialog.Builder(a);
        View dialogView = a.getLayoutInflater().inflate(R.layout.alert_forgot_password, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        EditText email = dialogView.findViewById(R.id.et_email);
        Button reset = dialogView.findViewById(R.id.bt_reset);
        Button cancel = dialogView.findViewById(R.id.bt_cancel);
        Button x = dialogView.findViewById(R.id.bt_x);

        AlertDialog dialogInterface = dialog.show();
        reset.setOnClickListener(view -> FirebaseAuth.getInstance().
                sendPasswordResetEmail(email.getText().toString()).
                addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Snackbar.make(constraintLayout, a.getResources().getString(R.string.link_is_sent), Snackbar.LENGTH_SHORT);
                        dialogInterface.dismiss();
                    }
                    else{
                        Snackbar.make(constraintLayout, task.getException().getMessage(), Snackbar.LENGTH_SHORT);
                    }
                }));


        cancel.setOnClickListener(view -> {
            dialogInterface.dismiss();
        });

        x.setOnClickListener(view -> dialogInterface.dismiss());
    }

    public static void bannerFullScreen( Activity a, String link) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(a);
        View dialogView = a.getLayoutInflater().inflate(R.layout.alert_banner_fullscreen, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        ImageView banner = dialogView.findViewById(R.id.iv_fullscreen);
        Button x = dialogView.findViewById(R.id.bt_x);

        AlertDialog dialogInterface = dialog.show();

        Picasso.get()
                .load(link.trim())
                .into(banner);

        x.setOnClickListener(view -> dialogInterface.dismiss());
    }

    public static void bannerFullScreenLocal( Activity a, String link) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(a);
        View dialogView = a.getLayoutInflater().inflate(R.layout.alert_banner_fullscreen, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        ImageView banner = dialogView.findViewById(R.id.iv_fullscreen);
        Button x = dialogView.findViewById(R.id.bt_x);

        AlertDialog dialogInterface = dialog.show();

        Picasso.get()
                .load(new File(link.trim()))
                .into(banner);

        x.setOnClickListener(view -> dialogInterface.dismiss());
    }

    public static void pdfPreviewLocal( Activity a, String link) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(a);
        View dialogView = a.getLayoutInflater().inflate(R.layout.alert_pdf_view, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        PDFView pdfView = dialogView.findViewById(R.id.pdfView);

        Button x = dialogView.findViewById(R.id.bt_x);


        AlertDialog dialogInterface = dialog.show();

        pdfView.fromFile(new File(link)).load();

        x.setOnClickListener(view -> dialogInterface.dismiss());
    }
}
