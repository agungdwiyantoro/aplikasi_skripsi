package com.example.agung.PPK_UNY_Mobile.searchAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agung.PPK_UNY_Mobile.R;
import com.example.agung.PPK_UNY_Mobile.model.model_advertisement;

import java.util.ArrayList;
import java.util.List;

import static com.example.agung.PPK_UNY_Mobile.utils.utils.timeMilisToDate_with_hours;

public class advertisementRecyclerAdapter extends RecyclerView.Adapter<advertisementRecyclerAdapter.MyViewHolder> implements Filterable {
    private List<model_advertisement> listModelAd, filteredListModelAd;
    private String[] categories = {"Berita","Tips Karir", "Job Seeker", "Panggilan Test"};
    private boolean true_false = true;

    private final String TAG = "advertisementAdapter";


    public advertisementRecyclerAdapter(List<model_advertisement> listModelAd) {
        this.listModelAd = listModelAd;
        filteredListModelAd = listModelAd;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.advertisement_items, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        model_advertisement m_a = filteredListModelAd.get(i);
        myViewHolder.informationName.setText(m_a.getInformasi_name());
        myViewHolder.informationCategory.setText(m_a.getCategory());
        myViewHolder.by.setText(m_a.getBy());
        myViewHolder.informationDate.setText(timeMilisToDate_with_hours(m_a.getDateCreated()));
        //myViewHolder.detail.setOnClickListener(view -> toLowongan(view.getContext(), m_a.getInformasiID(), m_a.getDetail()));
        myViewHolder.itemView.setOnClickListener(view -> toLowongan(view.getContext(), m_a.getInformasiID(), m_a.getDetail()));

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                if(charSequence==null || charSequence.length()==0){
                    results.values = listModelAd;
                    results.count = listModelAd.size();
                }
                else {
                    List<model_advertisement> filteredData = new ArrayList<>();
                    for (String category : categories) {
                        if (category.equals(charSequence.toString())) {
                            true_false = false;
                            break;
                        }
                    }
                    if (true_false) {
                        for (model_advertisement data : listModelAd) {
                            if (data.getInformasi_name().toLowerCase().contains(charSequence.toString())) {
                                filteredData.add(data);
                            }
                        }
                        results.values = filteredData;
                        results.count = filteredData.size();
                    }
                    else{
                        for (model_advertisement data : listModelAd) {
                            if (data.getCategory().contains(charSequence.toString())) {
                                filteredData.add(data);
                            }
                        }
                        results.values = filteredData;
                        results.count = filteredData.size();
                    }
                }


                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredListModelAd = (List<model_advertisement>) filterResults.values;
                notifyDataSetChanged();
            }
        } ;
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView informationName, by, informationCategory, informationDate;
        //Button detail;
        MyViewHolder(View itemView) {
            super(itemView);
            informationName = itemView.findViewById(R.id.information_name);
            informationCategory = itemView.findViewById(R.id.information_category);
            by= itemView.findViewById(R.id.by);
            informationDate = itemView.findViewById(R.id.information_date);
            //detail = itemView.findViewById(R.id.information_detail);
        }
    }

    @Override
    public int getItemCount() {
        return filteredListModelAd.size();
    }

    private void toLowongan(Context context, String namaLowongan, String linkDownload){
        Intent lowongan = new Intent(context, com.example.agung.PPK_UNY_Mobile.lowongan.class);
        lowongan.putExtra("namaLowongan",namaLowongan);
        lowongan.putExtra("linkDownload",linkDownload);
        context.startActivity(lowongan);
    }
}
