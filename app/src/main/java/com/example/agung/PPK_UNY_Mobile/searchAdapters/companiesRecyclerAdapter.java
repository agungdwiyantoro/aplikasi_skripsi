package com.example.agung.PPK_UNY_Mobile.searchAdapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agung.PPK_UNY_Mobile.R;
import com.example.agung.PPK_UNY_Mobile.companiesJobs.jobsCompaniesJob;
import com.example.agung.PPK_UNY_Mobile.model.model_company_list;
import com.example.agung.PPK_UNY_Mobile.webview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class companiesRecyclerAdapter extends RecyclerView.Adapter<companiesRecyclerAdapter.MyViewHolder> implements Filterable {

    private List<model_company_list> jobList, filteredListModelJob;
        private Context mContext;
        private Map<String, Integer> countMap = new HashMap<>();

        public companiesRecyclerAdapter(Context mContext, List<model_company_list> jobList){
            this.jobList = jobList;
            filteredListModelJob = jobList;
            this.mContext = mContext;
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
                        List<model_company_list> filteredData = new ArrayList<>();

                        for (model_company_list data : jobList) {
                            if (data.getCompanyName().toLowerCase().contains(charSequence.toString())) {
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
                    filteredListModelJob= (List<model_company_list>) filterResults.values;
                    notifyDataSetChanged();
                }
            } ;
        }

        static class MyViewHolder extends RecyclerView.ViewHolder{
            TextView companyName, companyAddress, companyEmail, companyJumlahLowongan,  companyDescription;
            ImageView companyLogo;

            Button companyWebsite;

            MyViewHolder(View itemView){
                super(itemView);
                companyLogo = itemView.findViewById(R.id.company_logo);
                companyName = itemView.findViewById(R.id.company_name);
                companyAddress = itemView.findViewById(R.id.company_address);
                companyEmail= itemView.findViewById(R.id.company_email);
                companyWebsite = itemView.findViewById(R.id.company_website);
                companyDescription = itemView.findViewById(R.id.company_description);
            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.company_list, viewGroup, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
            model_company_list mj = filteredListModelJob.get(i);

            Glide.with(mContext).load(mj.getCompanyLogo()).into(myViewHolder.companyLogo);
            myViewHolder.companyName.setText(mj.getCompanyName());
            myViewHolder.companyAddress.setText(mj.getCompanyAddress());
            myViewHolder.companyEmail.setText(mj.getCompanyEmail());

            if(mj.getCompanyDescription()!=null){
                if(!mj.getCompanyWebsite().trim().equals("")) {
                    myViewHolder.companyDescription.setVisibility(View.VISIBLE);
                    myViewHolder.companyDescription.setText(mj.getCompanyDescription());
                }
                else{
                    myViewHolder.companyDescription.setVisibility(View.GONE);
                }
            }


            if(mj.getCompanyWebsite()!=null){
                if(!mj.getCompanyWebsite().trim().equals("")) {
                    myViewHolder.companyWebsite.setVisibility(View.VISIBLE);
                    myViewHolder.companyWebsite.setText(mj.getCompanyWebsite());
                }
                else{
                    myViewHolder.companyWebsite.setVisibility(View.GONE);
                }

            }

            myViewHolder.companyWebsite.setOnClickListener(view -> sentData(mContext, mj.getCompanyWebsite()));

            myViewHolder.itemView.setOnClickListener(view -> sentData(mj.getCompanyID(), mj.getCompanyName(), mj.getCompanyEmail()));
        }

        @Override
        public int getItemCount() {
            return filteredListModelJob.size();
        }


    private void sentData(Context context, String url){
        Intent intent = new Intent(context, webview.class);
        intent.putExtra(context.getString(R.string.URL_KEY), url);
        context.startActivity(intent);
    }

    private void sentData( String companyID, String companyName, String companyEmail){
        Log.d("CompaniesAdapter", "data is sent");
        Intent toJobs = new Intent(mContext, jobsCompaniesJob.class);
        toJobs.putExtra("companyID", companyID);
        toJobs.putExtra("companyName", companyName);
        //toJobs.putExtra("companyEmail", companyEmail);
        mContext.startActivity(toJobs);
    }




    private void duplicates(List<String> job_list){
        for (String item: job_list) {
            if (countMap.containsKey(item))
                countMap.put(item, countMap.get(item) + 1);
            else
                countMap.put(item, 1);
        }
    }

}
