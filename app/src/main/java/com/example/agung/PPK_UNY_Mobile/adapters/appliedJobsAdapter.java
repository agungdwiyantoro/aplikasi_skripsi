package com.example.agung.PPK_UNY_Mobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agung.PPK_UNY_Mobile.R;
import com.example.agung.PPK_UNY_Mobile.model.model_appliedJobs;

import java.util.Date;
import java.util.List;

import static com.example.agung.PPK_UNY_Mobile.utils.utils.timeMilisToDate_appliedJobs;
import static com.example.agung.PPK_UNY_Mobile.utils.utils.timeMilisToDate_with_hours;

public class appliedJobsAdapter extends RecyclerView.Adapter<appliedJobsAdapter.MyViewHolder> {

    private List<model_appliedJobs> apJobsList;
    private Context context;


    public appliedJobsAdapter(List<model_appliedJobs> apJobList, Context context){
        this.apJobsList = apJobList;
        this.context = context;
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView companyName, jobName, date;

        MyViewHolder(View itemView){
            super(itemView);
            jobName = itemView.findViewById(R.id.job_name);
            companyName = itemView.findViewById(R.id.company_name);
            date = itemView.findViewById(R.id.job_date);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_applied_jobs, viewGroup, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        model_appliedJobs apJobs = apJobsList.get(i);
        myViewHolder.companyName.setText(apJobs.getCompanyName());
        myViewHolder.jobName.setText(apJobs.getJobName());
        Date date = timeMilisToDate_appliedJobs(apJobs.getDate());
        if(date!=null) {
            myViewHolder.date.setText(timeMilisToDate_with_hours(date));
        }

        myViewHolder.itemView.setOnClickListener(view -> {
            toLowongan(context, apJobs.getJobName(), apJobs.getDetail());
        });

    }

    @Override
    public int getItemCount() {
        return apJobsList.size();
    }

    private void toLowongan(Context context, String namaLowongan, String linkDownload){
        Intent lowongan = new Intent(context, com.example.agung.PPK_UNY_Mobile.lowongan.class);
        lowongan.putExtra("namaLowongan",namaLowongan);
        lowongan.putExtra("linkDownload",linkDownload);
        context.startActivity(lowongan);
    }

}
