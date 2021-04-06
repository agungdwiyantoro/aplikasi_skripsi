package com.example.agung.PPK_UNY_Mobile.guessAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agung.PPK_UNY_Mobile.R;
import com.example.agung.PPK_UNY_Mobile.model.model_job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.agung.PPK_UNY_Mobile.alertDialogs.alertDialogs.DialogFormGuess;

public class guessJobsRecyclerAdapter extends RecyclerView.Adapter<guessJobsRecyclerAdapter.MyViewHolder> implements Filterable {

    private List<model_job> jobList, filteredListModelJob;
    private Activity activity;

    public guessJobsRecyclerAdapter(List<model_job> jobList, Activity activity){
        this.jobList = jobList;
        filteredListModelJob = jobList;
        this.activity = activity;

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

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView jobName, companyName, quickApply, jobDate, jobExpired;
        ToggleButton save;
        Button pelamar;//, detail;
        FrameLayout frame_expired;
        MyViewHolder(View itemView){
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
        final String getDetail = mj.getDetail();

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

        if(!mj.isQuickApply()){
            myViewHolder.quickApply.setVisibility(View.INVISIBLE);
        }
        else{
            myViewHolder.quickApply.setVisibility(View.VISIBLE);
        }

        myViewHolder.itemView.setOnClickListener(view -> toLowongan(view.getContext(), jobID, getDetail));
        myViewHolder.save.setOnClickListener(view -> {
            myViewHolder.save.setChecked(false);
            DialogFormGuess(activity);
        });
        myViewHolder.quickApply.setOnClickListener(view ->    DialogFormGuess(activity));
    }

    @Override
    public int getItemCount() {
        return filteredListModelJob.size();
    }

    private String date_final(Date start, Date end){
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String startDate = sfd.format(start);
        String endDate = sfd.format(end);
        return  startDate + " - " + endDate;
    }

    private void toLowongan(Context context, String namaLowongan, String linkDownload){
        Intent lowongan = new Intent(context, com.example.agung.PPK_UNY_Mobile.lowongan.class);
        lowongan.putExtra("namaLowongan",namaLowongan);
        lowongan.putExtra("linkDownload",linkDownload);
        context.startActivity(lowongan);
    }


}
