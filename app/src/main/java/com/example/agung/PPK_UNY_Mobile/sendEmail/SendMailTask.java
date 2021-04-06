package com.example.agung.PPK_UNY_Mobile.sendEmail;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewGroup;

import com.example.agung.PPK_UNY_Mobile.interfaces.inputApplied;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;

public class SendMailTask extends AsyncTask<Object, Object, Object>{

    private ProgressDialog statusDialog;
    private WeakReference<ViewGroup> viewGroup;
    private WeakReference<Context> context;
    private inputApplied ia;
    private boolean status;

    public SendMailTask(Context context, ViewGroup viewGroup, inputApplied ia) {
        this.viewGroup = new WeakReference<>(viewGroup);
        this.context = new WeakReference<>(context);
        this.ia = ia;
    }

    protected void onPreExecute(){
        statusDialog = new ProgressDialog(context.get());
        statusDialog.setMessage("Getting ready...");
        statusDialog.setIndeterminate(false);
        statusDialog.setCancelable(false);
        statusDialog.show();
    }

    @Override
    protected Object doInBackground(Object... args) {


        boolean success;
        try {
            Log.i("SendMailTask", "About to instantiate GMail...");
            publishProgress("Processing input....");
            Gmail androidEmail = new Gmail(args[0].toString(),
                    args[1].toString(),  args[2].toString(), args[3].toString(),
                    args[4].toString());
            publishProgress("Preparing mail message....");
            androidEmail.createEmailMessage();
            publishProgress("Sending email....");
            androidEmail.sendEmail();
            publishProgress("Email Sent.");
            Log.i("SendMailTask", "Mail Sent.");
            success = true;
           // Config.mailSuccess="1";
        } catch (Exception e) {
            publishProgress(e.getMessage());
            Log.e("SendMailTask", e.getMessage(), e);
            success = false;
        }
        return success;
    }

    @Override
    public void onProgressUpdate(Object... values) {
        statusDialog.setMessage(values[0].toString());



    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        statusDialog.dismiss();
        if((boolean) o) {
            ia.statusSend();
            Snackbar.make(viewGroup.get(), "Your resume has been sent", Snackbar.LENGTH_SHORT).show();
        }
        else{
            Snackbar.make(viewGroup.get(), "Failed to send resume" + o.toString(), Snackbar.LENGTH_SHORT).show();
        }
    }
}