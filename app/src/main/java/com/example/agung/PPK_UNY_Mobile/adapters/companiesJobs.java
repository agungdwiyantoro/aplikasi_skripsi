package com.example.agung.PPK_UNY_Mobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.droidnet.DroidListener;
import com.example.agung.PPK_UNY_Mobile.BaseActivity;
import com.example.agung.PPK_UNY_Mobile.R;
import com.example.agung.PPK_UNY_Mobile.lowongan;
import com.example.agung.PPK_UNY_Mobile.model.model_job;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Date;

import static com.example.agung.PPK_UNY_Mobile.BaseActivity.getEmail;
import static com.example.agung.PPK_UNY_Mobile.MainActivity.sqLiteHelper;
import static com.example.agung.PPK_UNY_Mobile.alertDialogs.alertDialogs.DialogFormUploadResume;
import static com.example.agung.PPK_UNY_Mobile.utils.utils.date_final;

public class companiesJobs implements DroidListener {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private CollectionReference jobs_users = firebaseFirestore.collection("users_jobs").
            document(user.getUid()).
            collection("jobs");


    private final String TAG = "FirestoreAdapter";

    private Activity context;
    private ViewGroup viewGroup;

    private static boolean networkStatus = true;

    public companiesJobs(){

    }

    public companiesJobs(Activity context, ViewGroup viewGroup){
        this.context = context;
        this.viewGroup   = viewGroup;
    }

    public FirestoreRecyclerAdapter getJobList(String collectionID){
        Query query = firebaseFirestore.collection("companies_jobs").document(collectionID).collection(collectionID)
                .orderBy("dateCreated", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<model_job> response = new FirestoreRecyclerOptions.Builder<model_job>()
                .setQuery(query, model_job.class)
                .build();

        return new FirestoreRecyclerAdapter<model_job, ModelJob>(response) {
            @Override
            public void onBindViewHolder(@NonNull final ModelJob holder, int position, @NonNull final model_job model) {
                final DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());

                holder.companyName.setVisibility(View.GONE);
                holder.jobName.setText(model.getJobName());



                if(model.getStartDate()!=null) {
                    holder.jobDate.setText(date_final(model.getStartDate(), model.getEndDate()));
/*
                    if(new Date().compareTo(model.getEndDate())>0){
                       holder.frame_expired.setVisibility(View.VISIBLE);
                    }
                    else{
                        holder.frame_expired.setVisibility(View.GONE);
                    }
                      */

                }


                if(!model.isQuickApply()){
                    holder.quickApply.setVisibility(View.INVISIBLE);
                }
                else{
                    holder.quickApply.setVisibility(View.VISIBLE);
                }

                holder.save.setOnClickListener(view -> {
                    if(new Date().compareTo(model.getEndDate())>0) {
                        Snackbar.make(viewGroup, context.getString(R.string.jobexpired), Snackbar.LENGTH_SHORT).show();
                    }else {
                        if (holder.save.isChecked()) {
                            model_job mj = new model_job(model.getJobName(), model.getDetail(), model.getStartDate(), model.getEndDate(), model.getJobID(), model.isQuickApply(), model.getCompanyID(), model.getCompanyName(), model.getCompanyEmail(), new Date());
                            jobs_users.document(model.getJobID()).set(mj);
                        } else {
                            jobs_users.whereEqualTo("jobID", snapshot.getId())
                                    .get()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot cf : task.getResult()) {
                                                jobs_users.document(snapshot.getId()).delete();
                                            }
                                        }
                                    });
                        }
                    }
                });


                holder.quickApply.setOnClickListener(view -> {

                    /*
                    if(new Date().compareTo(model.getEndDate())>0) {
                        Snackbar.make(viewGroup, context.getString(R.string.jobexpired), Snackbar.LENGTH_SHORT).show();
                    }
                    else{
                        String email;
                        if (user.getProviderData().get(1).getProviderId().equals("facebook.com")) {
                            email = user.getProviderData().get(1).getEmail();
                        } else {
                            email = user.getEmail();
                        }

                        String path = "resumes/" + getEmail() + "/" + "resume(" + getEmail() + ")";
                        if (networkStatus) {
                            BaseActivity.metaData(meta_data -> {
                                if (meta_data != null) {
                                    DialogForm(context, () -> {
                                        BaseActivity.sentResume(context, model, viewGroup, sqLiteHelper, email);
                                    }, meta_data.getFileName(), meta_data.getDate(), model.getCompanyName());
                                } else {
                                    DialogFormUploadResume(context);
                                }
                            }, context, viewGroup, path);
                        }
                    }
                     */
                    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                    DocumentReference users = firebaseFirestore.
                            collection("users").
                            document(getEmail());

                    users.get().addOnCompleteListener(task -> {
                        if(task.getResult().getString("name")!=null ||
                                task.getResult().getString("birth_date")!=null ||
                                task.getResult().getString("birth_place")!=null ||
                                task.getResult().getString("handphone")!=null ||
                                task.getResult().getBoolean("jenisKelamin")!=null){

                            BaseActivity.sentResume(context, model, viewGroup, sqLiteHelper, getEmail());
                        }

                        else {
                            DialogFormUploadResume(context);
                        }
                    });
                });

                //holder.detail.setOnClickListener(view -> toLowongan(view.getContext(), model.getJobID(), model.getDetail()));

                holder.itemView.setOnClickListener(view -> toLowongan(view.getContext(), model.getJobID(), model.getDetail()));

                usersJob(snapshot, holder.save);
            }

            @NonNull
            @Override
            public ModelJob onCreateViewHolder(@NonNull ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.recycler_item, group, false);
                return new ModelJob(view);
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        networkStatus = isConnected;
    }

    public class ModelJob extends RecyclerView.ViewHolder {
        TextView jobName,  jobDate, quickApply, companyName;
        ToggleButton save;
        //Button detail ;
        //FrameLayout frame_expired;
        ModelJob(View itemView) {
            super(itemView);
            jobName = itemView.findViewById(R.id.job_name);
            companyName = itemView.findViewById(R.id.company_name);
            jobDate = itemView.findViewById(R.id.job_date);
            //jobExpired = itemView.findViewById(R.id.job_expired);
            save = itemView.findViewById(R.id.save);
            //detail = itemView.findViewById(R.id.job_detail);
            //frame_expired = itemView.findViewById(R.id.frame_expired);
            quickApply = itemView.findViewById(R.id.bt_quick_apply);
        }
    }

    private void toLowongan(Context context, String namaLowongan, String linkDownload){
        Intent lowongan = new Intent(context, lowongan.class);
        lowongan.putExtra("namaLowongan",namaLowongan);
        lowongan.putExtra("linkDownload",linkDownload);
        context.startActivity(lowongan);
    }

    private void usersJob(DocumentSnapshot snapshot, ToggleButton holder){
        jobs_users.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot cf : task.getResult()) {
                            if (snapshot.getId().equals(cf.getString("jobID"))) {
                                holder.setChecked(true);
                            }
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }
}



