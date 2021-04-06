package com.example.agung.PPK_UNY_Mobile.unused;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agung.PPK_UNY_Mobile.R;
import com.example.agung.PPK_UNY_Mobile.lowongan;
import com.example.agung.PPK_UNY_Mobile.model.model_job;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SearchAdapter {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

    public List<model_job> getJobs_list() {
        return jobs_list;
    }

    private List<model_job> jobs_list = new ArrayList<>();

    public FirestoreRecyclerAdapter getJobList(){
        final String TAG = "FirestoreAdapter";
        Log.d(TAG, "getJobList");

        Query query = db.collection("jobs").orderBy("endDate");

        final CollectionReference jobs_users = db.collection("users_jobs").document(currentFirebaseUser.getUid()).collection("jobs");

        FirestoreRecyclerOptions<model_job> response = new FirestoreRecyclerOptions.Builder<model_job>()
                .setQuery(query, model_job.class)
                .build();

        return new FirestoreRecyclerAdapter<model_job, ModelJob>(response) {
            @Override
            public void onBindViewHolder(@NonNull final ModelJob holder, int position, @NonNull final model_job model) {
                final DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());

                holder.jobName.setText(model.getJobName());

                if(model.getStartDate()!=null) {
                    holder.jobDate.setText(date_final(model.getStartDate(), model.getEndDate()));

                    if(new Date().compareTo(model.getEndDate())>0){
                        holder.frame_expired.setVisibility(View.VISIBLE);
                    }
                    else{
                        holder.frame_expired.setVisibility(View.GONE);
                    }
                }

                holder.save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(holder.save.isChecked())
                        {
                         //   model_job mj = new model_job(model.getJobName(), model.getDetail(), model.getStartDate(), model.getEndDate(), model.getJobID(),model.isQuickApply());
                           // jobs_users.document(model.getJobID()).set(mj);

                        }
                        else{

                            jobs_users.whereEqualTo("jobID", snapshot.getId())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()){
                                                for (QueryDocumentSnapshot cf : task.getResult()) {
                                                //    if(snapshot.getId().equals(cf.getString("jobID"))) {
                                                        //Log.d("documentxx", cf.getString("jobID"));

                                                        jobs_users.document(snapshot.getId()).delete();
                                                  //  }
                                                }
                                            }
                                            else{
                                                //Log.d(TAG, "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });
                        }
                    }
                });
/*
                holder.detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toLowongan(view.getContext(), model.getJobID(), model.getDetail());
                    }
                });
*/

                    jobs_users.get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for (QueryDocumentSnapshot cf : task.getResult()) {
                                            if(snapshot.getId().equals(cf.getString("jobID"))) {
                                                Log.d("documentxx", cf.getString("jobID"));
                                                holder.save.setChecked(true);
                                            }
                                        }
                                    }
                                    else{
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });
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

    public class ModelJob extends RecyclerView.ViewHolder {
        TextView jobName, jobDate, jobExpired;
        ToggleButton save;
        //Button pelamar, detail;
        FrameLayout frame_expired;
        ModelJob(View itemView) {
            super(itemView);
            jobName = itemView.findViewById(R.id.job_name);
            //jobDescripton = itemView.findViewById(R.id.job_description);
            //jobCompany = itemView.findViewById(R.id.company_name);
            jobDate = itemView.findViewById(R.id.job_date);
            //jobExpired = itemView.findViewById(R.id.job_expired);
            save = itemView.findViewById(R.id.save);
            //detail = itemView.findViewById(R.id.job_detail);
          //  pelamar = itemView.findViewById(R.id.jumlah_pelamar);
            //frame_expired = itemView.findViewById(R.id.frame_expired);
        }
    }

    private String date_final(Date start, Date end){
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String startDate = sfd.format(start);
        String endDate = sfd.format(end);

        /*
        Calendar cal = new GregorianCalendar(2015,05,10); // Month values are 0(Jan) - 11 (Dec). So for June it is 05
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);      // 0 - 11
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        */

        //Log.d(TAG, "Rstart and end date " +startDate+ ", " + endDate);
        return  startDate + " - " + endDate;
    }

    private void toLowongan(Context context, String namaLowongan, String linkDownload){
        Intent lowongan = new Intent(context, lowongan.class);
        lowongan.putExtra("namaLowongan",namaLowongan);
        lowongan.putExtra("linkDownload",linkDownload);
        context.startActivity(lowongan);
    }
}



