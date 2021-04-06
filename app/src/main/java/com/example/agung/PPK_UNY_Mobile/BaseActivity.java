package com.example.agung.PPK_UNY_Mobile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.droidnet.DroidListener;
import com.droidnet.DroidNet;
import com.example.agung.PPK_UNY_Mobile.SQLiteDatabase.SQLiteHelper;
import com.example.agung.PPK_UNY_Mobile.interfaces.callback;
import com.example.agung.PPK_UNY_Mobile.interfaces.callbackDiploma;
import com.example.agung.PPK_UNY_Mobile.model.model_IKA_member;
import com.example.agung.PPK_UNY_Mobile.model.model_appliedJobs;
import com.example.agung.PPK_UNY_Mobile.model.model_file_meta_data;
import com.example.agung.PPK_UNY_Mobile.model.model_job;
import com.example.agung.PPK_UNY_Mobile.sendEmail.SendMailTask;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static com.example.agung.PPK_UNY_Mobile.utils.utils.timeMilisToDate_with_hours;

public class BaseActivity extends AppCompatActivity implements DroidListener {

    private final String TAG = this.getClass().getName();

    private SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    private ProgressDialog mProgressDialog;
    private DroidNet mDroidNet;

    static boolean networkSatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDroidNet = DroidNet.getInstance();
        mDroidNet.addInternetConnectivityListener(this);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void showProgressDialog(){
        if(mProgressDialog==null){
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getResources().getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog(){
        if(mProgressDialog !=null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }

    public void showSnackbar(View view, String text){
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    }

    public static void showSnackbarstatic(View view, String text){
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    }

    public void showSnackbarAddCallback(View view, String text, Activity activity){
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        snackbar.addCallback(new Snackbar.Callback(){
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                activity.finish();
            }
        }).show();
    }

    public static void showSnackbarAddCallbackStatic(View view, String text, Activity activity){
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        snackbar.addCallback(new Snackbar.Callback(){
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                activity.finish();
            }
        }).show();
    }

    public void showSnackbarToActivity(View view, String text, Activity x, Class y){
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        snackbar.addCallback(new Snackbar.Callback(){
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                toActivity(x, y);
            }
        }).show();
    }

    public static void toActivityNotClear(Activity activity, Class toClass){
        Intent intent = new Intent(activity, toClass);
        activity.startActivity(intent);
    }

    public static void toActivity(Activity activity, Class toClass){
        Intent intent = new Intent(activity, toClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static void toActivityClear(Activity activity, Class toClass){
        Intent intent = new Intent(activity, toClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

    public static void sentResume(Context context,model_job model, ViewGroup viewGroup, SQLiteHelper sqLiteHelper, String email){
        if(networkSatus){
            if(sqLiteHelper.getJobID(model.getJobID(), email)==0) {
                Snackbar.make(viewGroup, "ACCESS DENIED" , Snackbar.LENGTH_SHORT).show();
                //model_appliedJobs apJobs = new model_appliedJobs( model.getCompanyName(),model.getJobName(), timeMilisToDate_with_hours(new Date()), model.getJobID(), model.getDetail());
              //  sqLiteHelper.insertData(apJobs, email);
               // new SendMailTask(context, viewGroup).execute("agung.dwiyantoro@gmail.com", "anjing235re5", model.getCompanyEmail(), "Lamaran Pekerjaan", "Kami meneruskan permohonan pekerjaan dari klien kami <b>" + email + "</b> yang melamar untuk pekerjaan sebagai <b>" + model.getJobName() + "</b> di <b>" + model.getCompanyName() + "</b> <br> <br>" + "Resume pemohon : " + "https://storage.cloud.google.com/ppk-uny-mobile.appspot.com/resumes/" + email + "/" + "resume(" + email + ")" + "<br> <br> " + " Tim Pusat Pengembangan Karir UNY");

            }
            else{
                Snackbar.make(viewGroup, "ACCESS DENIED" , Snackbar.LENGTH_SHORT).show();
                //Snackbar.make(viewGroup, context.getResources().getString(R.string.already_applied) , Snackbar.LENGTH_SHORT).show();
            }
        }
        else{
            Snackbar.make(viewGroup, context.getResources().getString(R.string.no_internet) , Snackbar.LENGTH_SHORT).show();
        }
    }

    public static void checkSentResume(Context context, ViewGroup viewGroup, model_job model){
        FirebaseFirestore  fs = FirebaseFirestore.getInstance();
        DocumentReference applied_jobs =  fs.collection("users_jobs")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .collection("applied")
                .document(model.getJobID());

        applied_jobs
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        if(task.getResult().getString("jobID")!=null){
                            Snackbar.make(viewGroup, context.getResources().getString(R.string.already_applied) , Snackbar.LENGTH_SHORT).show();
                            Log.d("checkSentResume", "Sudah ada");
                        }
                        else{

                            new SendMailTask(context, viewGroup, () -> {
                                model_appliedJobs apJobs = new model_appliedJobs( model.getCompanyName(),model.getJobName(), timeMilisToDate_with_hours(new Date()), model.getJobID(), model.getDetail());
                                applied_jobs.set(apJobs);
                            });//.execute("agung.dwiyantoro@gmail.com", "anjing235re5", "ppkunymobile@gmail.com"/*model.getCompanyEmail()*/, "Lamaran Pekerjaan", "Kami meneruskan permohonan pekerjaan dari klien kami <b>" + getEmail() + "</b> yang melamar untuk pekerjaan sebagai <b>" + model.getJobName() + "</b> di <b>" + model.getCompanyName() + "</b> <br> <br>" + "Resume pemohon : " + "https://storage.cloud.google.com/ppk-uny-mobile.appspot.com/resumes/" + getEmail() + "/" + "resume(" + getEmail() + ")" + "<br> <br> " + " Tim Pusat Pengembangan Karir UNY");
                            Snackbar.make(viewGroup, "Your resume has been sent", Snackbar.LENGTH_SHORT).show();
                            Log.d("checkSentResume", "tidak ada");
                        }
                    }
                    else{
                        Snackbar.make(viewGroup, context.getResources().getString(R.string.no_internet) , Snackbar.LENGTH_SHORT).show();
                        Log.d("checkSentResume", "check gagal");
                    }
                });
    }



    public static void metaData(callback cb, Context context, ViewGroup viewGroup, String path){
        if(networkSatus){
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference().child(path);
            storageReference.getMetadata().addOnSuccessListener(storageMetadata -> {
                cb.getMetaData(new model_file_meta_data(
                        storageMetadata.getCustomMetadata(context.getString(R.string.fullName)),
                        storageMetadata.getCustomMetadata(context.getString(R.string.phoneNumber)),
                        storageMetadata.getCustomMetadata(context.getString(R.string.fileNameOri)),
                        storageMetadata.getCustomMetadata(context.getString(R.string.email)),
                        new Date(storageMetadata.getCreationTimeMillis())));

            }).addOnFailureListener(e -> {
                cb.getMetaData(null);
            });
        }
        else{
            Snackbar.make(viewGroup, context.getResources().getString(R.string.no_internet) , Snackbar.LENGTH_SHORT).show();
        }

    }

    public static void metaDataDiploma(callbackDiploma cbDiploma, Context context, ViewGroup viewGroup, String path){
        if(networkSatus){
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference().child(path);
            storageReference.getMetadata().addOnSuccessListener(storageMetadata -> cbDiploma.getMetaDataDiploma(new model_IKA_member(
                    storageMetadata.getCustomMetadata(context.getString(R.string.fullName)),
                    storageMetadata.getCustomMetadata(context.getString(R.string.NIM)),
                    storageMetadata.getCustomMetadata(context.getString(R.string.phoneNumber)),
                    storageMetadata.getCustomMetadata(context.getString(R.string.key_diploma_name)),
                    new Date(storageMetadata.getCreationTimeMillis()))))
                    .addOnFailureListener(e -> cbDiploma.getMetaDataDiploma(null));
        }
        else{
            Snackbar.make(viewGroup, context.getResources().getString(R.string.no_internet) , Snackbar.LENGTH_SHORT).show();
        }
    }

    public static void metaDataNew(callback cb, Context context, ViewGroup viewGroup, String path){
        if(networkSatus){
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference().child(path);
            storageReference.getMetadata().addOnSuccessListener(storageMetadata -> {
                cb.getMetaData(new model_file_meta_data(
                        storageMetadata.getCustomMetadata(context.getString(R.string.fileNameOri)),
                        storageMetadata.getCustomMetadata(context.getString(R.string.birth_date))));
                      //  new Date(storageMetadata.getCreationTimeMillis())));

            }).addOnFailureListener(e -> {
                cb.getMetaData(null);
            });
        }
        else{
            Snackbar.make(viewGroup, context.getResources().getString(R.string.no_internet) , Snackbar.LENGTH_SHORT).show();
        }

    }

    public void setAppLocale(String localCode){
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(localCode.toLowerCase()));
        res.updateConfiguration(conf, dm);
    }



    public void saveSubscription(String key, String value){
        editor= sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getSubscription(String key, String defaultValue){
        return sharedPref.getString(key, defaultValue);
    }

    public static String getEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getProviderData().get(1).getProviderId().equals("facebook.com")) {
            return user.getProviderData().get(1).getEmail();
        } else {
            return user.getEmail();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDroidNet.removeInternetConnectivityChangeListener(this);
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        networkSatus = isConnected;
    }
}
