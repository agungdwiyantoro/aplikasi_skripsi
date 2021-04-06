package com.example.agung.PPK_UNY_Mobile.searchAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.droidnet.DroidListener;
import com.example.agung.PPK_UNY_Mobile.BaseActivity;
import com.example.agung.PPK_UNY_Mobile.R;
import com.example.agung.PPK_UNY_Mobile.model.model_job;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.agung.PPK_UNY_Mobile.BaseActivity.getEmail;
import static com.example.agung.PPK_UNY_Mobile.MainActivity.sqLiteHelper;
import static com.example.agung.PPK_UNY_Mobile.alertDialogs.alertDialogs.DialogFormUploadResume;
import static com.example.agung.PPK_UNY_Mobile.utils.utils.date_final;


public class jobsRecyclerAdapter extends RecyclerView.Adapter<jobsRecyclerAdapter.MyViewHolder> implements Filterable, DroidListener {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private List<model_job> jobList, filteredListModelJob;

    private CollectionReference users_jobs = firebaseFirestore.collection("users_jobs").document(user.getUid()).collection("jobs");

    private ViewGroup viewGroup;
    private Activity context;

    private static boolean networkStatus = true;


    public jobsRecyclerAdapter(Activity context, List<model_job> jobList, ViewGroup viewGroup){
        this.context = context;
        this.jobList = jobList;
        this.viewGroup = viewGroup;
        filteredListModelJob = jobList;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                if(charSequence==null || charSequence.length()==0){
                    results.values = jobList;
                    results.count = jobList.size();
                }
                else {
                    List<model_job> filteredData = new ArrayList<>();

                        for (model_job data : jobList) {
                            if (data.getJobName().toLowerCase().contains(charSequence.toString())) {
                                filteredData.add(data);
                            }
                        }
                        results.values = filteredData;
                        results.count = filteredData.size();
                    }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredListModelJob= (List<model_job>) filterResults.values;
                notifyDataSetChanged();
            }
        } ;
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        networkStatus = isConnected;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView jobName, jobDate, jobExpired, quickApply, companyName;
        ToggleButton save;
        //Button detail;
        FrameLayout frame_expired;
        MyViewHolder(View itemView){
            super(itemView);
            jobName = itemView.findViewById(R.id.job_name);
            jobDate = itemView.findViewById(R.id.job_date);
            //jobExpired = itemView.findViewById(R.id.job_expired);
            save = itemView.findViewById(R.id.save);
            //detail = itemView.findViewById(R.id.job_detail);
            companyName = itemView.findViewById(R.id.company_name);
            //frame_expired = itemView.findViewById(R.id.frame_expired);
            quickApply = itemView.findViewById(R.id.bt_quick_apply);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        model_job mj = filteredListModelJob.get(i);

        final String jobID = mj.getJobID();

        myViewHolder.jobName.setText(mj.getJobName());
        myViewHolder.companyName.setText(mj.getCompanyName());

        if(mj.getStartDate()!=null) {
            myViewHolder.jobDate.setText(date_final(mj.getStartDate(), mj.getEndDate()));
            /*
            if(new Date().compareTo(mj.getEndDate())>0){
                myViewHolder.frame_expired.setVisibility(View.VISIBLE);
            }
            else{
                myViewHolder.frame_expired.setVisibility(View.GONE);
            }
             */
        }

        if (!mj.isQuickApply()) {
            myViewHolder.quickApply.setVisibility(View.INVISIBLE);
        } else {
            myViewHolder.quickApply.setVisibility(View.VISIBLE);
        }



        //myViewHolder.detail.setOnClickListener(view -> toLowongan(view.getContext(), mj.getJobID(), mj.getDetail()));

        myViewHolder.itemView.setOnClickListener(view -> toLowongan(view.getContext(), mj.getJobID(), mj.getDetail()));


        usersJob(jobID, myViewHolder.save);

        myViewHolder.save.setOnClickListener(new onClickListener(myViewHolder.save, mj));

        myViewHolder.quickApply.setOnClickListener(view -> {
            /*
            if(new Date().compareTo(mj.getEndDate())>0) {
                Snackbar.make(viewGroup, context.getString(R.string.jobexpired), Snackbar.LENGTH_SHORT).show();
            }else{
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
                                BaseActivity.sentResume(context, mj, viewGroup, sqLiteHelper, email);
                            }, meta_data.getFileName(), meta_data.getDate(), mj.getCompanyName());
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

                    BaseActivity.sentResume(context, mj, viewGroup, sqLiteHelper, getEmail());
                }

                else {
                    DialogFormUploadResume(context);
                }
            });
        });

    }

    @Override
    public int getItemCount() {
        return filteredListModelJob.size();
    }


    private void toLowongan(Context context, String namaLowongan, String linkDownload){
        Intent lowongan = new Intent(context, com.example.agung.PPK_UNY_Mobile.lowongan.class);
        lowongan.putExtra("namaLowongan",namaLowongan);
        lowongan.putExtra("linkDownload",linkDownload);
        context.startActivity(lowongan);
    }


    private class onClickListener implements View.OnClickListener {

        ToggleButton save;
        model_job mj;

        onClickListener(ToggleButton save, model_job mj){
            this.save = save;
            this.mj = mj;
        }

        @Override
        public void onClick(View view) {
            if(new Date().compareTo(mj.getEndDate())>0) {
                Snackbar.make(viewGroup, context.getString(R.string.jobexpired), Snackbar.LENGTH_SHORT).show();
            }else{
                if (save.isChecked()) {
                    model_job mjob = new model_job(mj.getJobName(), mj.getDetail(), mj.getStartDate(), mj.getEndDate(), mj.getJobID(), mj.isQuickApply(), mj.getCompanyID(), mj.getCompanyName(), mj.getCompanyEmail(), new Date());
                    users_jobs.document(mj.getJobID()).set(mjob);
                } else {
                    users_jobs.whereEqualTo("jobID", mj.getJobID())
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot cf : task.getResult()) {
                                        users_jobs.document(mj.getJobID()).delete();
                                    }
                                }
                            });
                }
            }
        }
    }

    private void usersJob(String jobID, ToggleButton holder){
        users_jobs.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot cf : task.getResult()) {
                            if (jobID.equals(cf.getString("jobID"))) {
                                holder.setChecked(true);
                            }
                        }
                    } else {
                        Log.d("ssscum", "Error getting documents: ", task.getException());
                    }
                });
    }


}
