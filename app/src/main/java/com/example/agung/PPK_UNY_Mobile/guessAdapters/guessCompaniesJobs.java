package com.example.agung.PPK_UNY_Mobile.guessAdapters;

import android.app.Activity;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import static com.example.agung.PPK_UNY_Mobile.alertDialogs.alertDialogs.DialogFormGuess;
import static com.example.agung.PPK_UNY_Mobile.utils.utils.date_final;

public class guessCompaniesJobs  {
    private Activity activity;
    private final String TAG = "FirestoreAdapter";
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public guessCompaniesJobs(){

    }

    public guessCompaniesJobs(Activity activity){
        this.activity = activity;
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

                holder.jobName.setText(model.getJobName());
                holder.companyName.setVisibility(View.GONE);


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
                    holder.save.setChecked(false);
                    DialogFormGuess(activity);
                });

                holder.quickApply.setOnClickListener(view -> {
                    holder.save.setChecked(false);
                    DialogFormGuess(activity);
                });

                holder.itemView.setOnClickListener(view -> toLowongan(view.getContext(), model.getJobID(), model.getDetail()));
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
        TextView jobName, companyName, quickApply, jobDate, jobExpired;
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
          //  frame_expired = itemView.findViewById(R.id.frame_expired);
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



