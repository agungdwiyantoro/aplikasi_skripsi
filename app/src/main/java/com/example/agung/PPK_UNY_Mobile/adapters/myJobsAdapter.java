package com.example.agung.PPK_UNY_Mobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.droidnet.DroidListener;
import com.example.agung.PPK_UNY_Mobile.BaseActivity;
import com.example.agung.PPK_UNY_Mobile.R;
import com.example.agung.PPK_UNY_Mobile.lowongan;
import com.example.agung.PPK_UNY_Mobile.model.model_job;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Date;
import java.util.List;

import static com.example.agung.PPK_UNY_Mobile.BaseActivity.getEmail;
import static com.example.agung.PPK_UNY_Mobile.MainActivity.sqLiteHelper;
import static com.example.agung.PPK_UNY_Mobile.alertDialogs.alertDialogs.DialogFormUploadResume;
import static com.example.agung.PPK_UNY_Mobile.utils.utils.date_final;

public class myJobsAdapter extends RecyclerView.Adapter<myJobsAdapter.ModelJob> implements DroidListener {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private List<model_job> myJobList;
    private CollectionReference jobs_users = firebaseFirestore.
            collection("users_jobs").
            document(user.getUid()).
            collection("jobs");

    private final String TAG = "FirestoreAdapter";

    private ViewGroup viewGroup;
    private Activity context;

    private static boolean networkStatus = true;


    public myJobsAdapter(){

    }

    public myJobsAdapter(Activity context, List<model_job> myJobList, ViewGroup viewGroup){
        this.context = context;
        this.viewGroup = viewGroup;
        this.myJobList = myJobList;
    }

            @Override
            public void onBindViewHolder(@NonNull final ModelJob holder, int position) {
                model_job model = myJobList.get(position);

                String snapshot = model.getJobID();
                holder.jobName.setText(model.getJobName());
                holder.companyName.setText(model.getCompanyName());


                if(model.getStartDate()!=null) {
                    holder.jobDate.setText(date_final(model.getStartDate(), model.getEndDate()));

                    if(new Date().compareTo(model.getEndDate())>0){
                        jobs_users.document(model.getJobID()).delete();
                        //holder.frame_expired.setVisibility(View.VISIBLE);
                    }
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
                            model_job mj = new model_job(model.getJobName(), model.getDetail(), model.getStartDate(), model.getEndDate(), model.getJobID(), model.isQuickApply(), model.getCompanyID(), model.getCompanyName(), model.getCompanyEmail());
                            jobs_users.document(model.getJobID()).set(mj);
                        } else {
                            jobs_users.whereEqualTo("jobID", snapshot)
                                    .get()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot cf : task.getResult()) {
                                                jobs_users.document(snapshot).delete();
                                            }
                                        }
                                    });
                        }
                    }
                });

                //holder.detail.setOnClickListener(view -> toLowongan(view.getContext(), model.getJobID(), model.getDetail()));
                holder.itemView.setOnClickListener(view -> toLowongan(view.getContext(), model.getJobID(), model.getDetail()));

                holder.quickApply.setOnClickListener(view -> {

                    /*
                    if(new Date().compareTo(model.getEndDate())>0) {
                        Snackbar.make(viewGroup, context.getString(R.string.jobexpired), Snackbar.LENGTH_SHORT).show();
                    }
                    else {

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
                                        //BaseActivity.sentResume(context, model, viewGroup, sqLiteHelper, email);
                                        Log.d(TAG, model.getJobID());
                                        BaseActivity.checkSentResume(context, viewGroup, model);;
                                    }, meta_data.getFileName(), meta_data.getDate(), model.getCompanyName());
                                } else {
                                    DialogFormUploadResume(context);
                                }
                            }, context, viewGroup, path);
                        } else {
                            Snackbar.make(viewGroup, context.getResources().getString(R.string.no_internet), Snackbar.LENGTH_SHORT).show();
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
                holder.save.setChecked(true);


/*
                firebaseFirestore
                        .collection("jobs")
                        .document(snapshot)
                        .get()
                        .addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()) {
                                Log.d("FUCKNIG", ", " + task1.getResult().getString("jobID"));
                                if(task1.getResult().getString("jobID")==null) {
                                    jobs_users.document(model.getJobID()).delete();
                                }

                                Log.d("YABOI", ", " + task1.getResult().getString("jobID"));
                                Log.d("YABOI2", ", " + model.getJobID());
                                if(task1.getResult().getString("jobID").equals(model.getJobID())) {

                                    model_job mj = new model_job(
                                            task1.getResult().getString("jobName"),
                                            task1.getResult().getString("detail"),
                                            task1.getResult().getDate("startDate"),
                                            task1.getResult().getDate("endDate"),
                                            task1.getResult().getString("jobID"),
                                            task1.getResult().getBoolean("quickApply"),
                                            task1.getResult().getString("companyID"),
                                            task1.getResult().getString("companyName"),
                                            task1.getResult().getString("companyEmail"));

                                    jobs_users.document(model.getJobID()).set(mj);
                                }
                            }
                        });

*/
                //jobs_users.document(cf.getString("jobID")).delete();




            }

    @Override
    public int getItemCount() {
        return myJobList.size();
    }

    @NonNull
    @Override
    public ModelJob onCreateViewHolder(@NonNull ViewGroup group, int i) {
        View view = LayoutInflater.from(group.getContext())
                .inflate(R.layout.recycler_item, group, false);
        return new ModelJob(view);
    }

    /*
    public FirestoreRecyclerAdapter getJobList(){
        Query query = jobs_users.orderBy("dateCreated", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<model_job> response = new FirestoreRecyclerOptions.Builder<model_job>()
                .setQuery(query, model_job.class)
                .build();

        return new FirestoreRecyclerAdapter<model_job, ModelJob>(response) {
            @Override
            public void onBindViewHolder(@NonNull final ModelJob holder, int position, @NonNull final model_job model) {
                final DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
                holder.jobName.setText(model.getJobName());
                holder.companyName.setText(model.getCompanyName());

                if(model.getStartDate()!=null) {
                    holder.jobDate.setText(date_final(model.getStartDate(), model.getEndDate()));

                    if(new Date().compareTo(model.getEndDate())>0){
                        holder.frame_expired.setVisibility(View.VISIBLE);
                    }
                    else{
                        holder.frame_expired.setVisibility(View.GONE);
                    }
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
                            model_job mj = new model_job(model.getJobName(), model.getDetail(), model.getStartDate(), model.getEndDate(), model.getJobID(), model.isQuickApply(), model.getCompanyID(), model.getCompanyName(), model.getCompanyEmail());
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

                holder.detail.setOnClickListener(view -> toLowongan(view.getContext(), model.getJobID(), model.getDetail()));

                holder.quickApply.setOnClickListener(view -> {
                    if(new Date().compareTo(model.getEndDate())>0) {
                        Snackbar.make(viewGroup, context.getString(R.string.jobexpired), Snackbar.LENGTH_SHORT).show();
                    }
                    else {

                        String email;
                        if (user.getProviderData().get(1).getProviderId().equals("facebook.com")) {
                            email = user.getProviderData().get(1).getEmail();
                        } else {
                            email = user.getEmail();
                        }

                        if (networkStatus) {
                            BaseActivity.metaData(meta_data -> {
                                if (meta_data != null) {
                                    DialogForm(context, () -> {
                                        BaseActivity.sentResume(context, model, viewGroup, sqLiteHelper, email);
                                    }, meta_data.getFileName(), meta_data.getDate(), model.getCompanyName());
                                } else {
                                    DialogFormUploadResume(context);
                                }
                            }, context, viewGroup);
                        } else {
                            Snackbar.make(viewGroup, context.getResources().getString(R.string.no_internet), Snackbar.LENGTH_SHORT).show();
                        }
                    }

                });
                holder.save.setChecked(true);



                firebaseFirestore

                        .collection("jobs")

                        .document(snapshot.getId())

                        .get()

                        .addOnCompleteListener(task1 -> {

                            if(task1.isSuccessful()) {
                                Log.d("FUCKNIG", ", " + task1.getResult().getString("jobID"));
                               if(task1.getResult().getString("jobID")==null) {
                                   jobs_users.document(snapshot.getId()).delete();
                               }

                            }

                        });


                                            //jobs_users.document(cf.getString("jobID")).delete();




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
*/
    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        networkStatus =  isConnected;
    }

    static class ModelJob extends RecyclerView.ViewHolder {
        TextView jobName, jobDate, jobExpired, quickApply, companyName;
        ToggleButton save;
        //Button detail;
        FrameLayout frame_expired;
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

}



