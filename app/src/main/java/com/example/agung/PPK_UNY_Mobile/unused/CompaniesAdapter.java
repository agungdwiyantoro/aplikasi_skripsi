package com.example.agung.PPK_UNY_Mobile.unused;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agung.PPK_UNY_Mobile.R;
import com.example.agung.PPK_UNY_Mobile.companiesJobs.jobs;
import com.example.agung.PPK_UNY_Mobile.model.model_company_list;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

public class CompaniesAdapter {
    private Context mContext;
    private FragmentActivity mFragmentActivity;


    public CompaniesAdapter(Context context){
        mContext = context;
    }

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private int count = 0;
    private String final_count;
    private ArrayList<String> id_job_temp = new ArrayList<>();
    private Map<String, Integer> countMap = new HashMap<>();
    private View.OnClickListener mOnItemClickListener;
    private CollectionReference companiesJobs =   db.collection("companies_jobs");

    public FirestoreRecyclerAdapter getJobList(final String root){

        final Query query = db.collection(root);

        FirestoreRecyclerOptions<model_company_list> response = new FirestoreRecyclerOptions.Builder<model_company_list>()
                .setQuery(query, model_company_list.class)
                .build();

        return new FirestoreRecyclerAdapter<model_company_list, ModelJob>(response) {
            @Override
            public void onBindViewHolder(@NonNull final ModelJob holder, int position, @NonNull final model_company_list model) {


                holder.companyName.setText(model.getCompanyName());
                holder.nJobVacancies.setText(model.getnJob_vacancies());
                Glide.with(mContext)
                        .load(model.getCompanyLogo())
                        .into(holder.companyLogo);
                holder.companyAddress.setText(model.getCompanyAddress());
                holder.companyEmail.setText(model.getCompanyEmail());
                if(model.getCompanyDescription()!=null){
                    holder.companyDescription.setVisibility(View.VISIBLE);
                    holder.companyDescription.setText(model.getCompanyDescription());
                }
                else{
                    holder.companyDescription.setVisibility(View.GONE);
                }
                if(model.getCompanyWebsite()!=null){
                    holder.companyWebsite.setVisibility(View.VISIBLE);
                    holder.companyWebsite.setText(model.getCompanyWebsite());
                }
                else{
                    holder.companyDescription.setVisibility(View.GONE);
                }
                final DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sentData(snapshot.getId(), model.getCompanyName());
                    }
                });


                companiesJobs
                        .document(snapshot.getId())
                        .collection(snapshot.getId())
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if(e != null){
                            Log.d("ERROR", e.getMessage());
                            return;
                        }
                        if(queryDocumentSnapshots!=null && !queryDocumentSnapshots.isEmpty()){
                            companiesJobs
                                    .document(snapshot.getId())
                                    .collection(snapshot.getId())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            for (QueryDocumentSnapshot cf : Objects.requireNonNull(task.getResult())) {
                                                id_job_temp.add(cf.getString("companyID"));
                                                Log.d("ArrayList " , " " + snapshot.getId());
                                                count++;
                                            }
                                            duplicates(id_job_temp);
                                            String nJob = countMap.get(snapshot.getId()) + " lowongan";
                                            Log.d("nJob ", nJob);
                                            holder.nJobVacancies.setText(nJob);
                                            id_job_temp.clear();
                                            countMap.clear();
                                        }
                                    });
                            Log.d("companiesJob", "UPDATED");
                        }

                    }
                });

            }

            @NonNull
            @Override
            public ModelJob onCreateViewHolder(@NonNull ViewGroup group, final int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.company_list, group, false);


                return new ModelJob(view);
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };
    }

    public class ModelJob extends RecyclerView.ViewHolder {
        TextView nJobVacancies,  companyName, companyAddress, companyEmail, companyDescription, companyWebsite;
        ImageView companyLogo;
        ModelJob(View itemView) {
            super(itemView);
            companyLogo = itemView.findViewById(R.id.company_logo);
            companyName = itemView.findViewById(R.id.company_name);
            companyAddress = itemView.findViewById(R.id.company_address);
            companyEmail = itemView.findViewById(R.id.company_email);
            companyDescription = itemView.findViewById(R.id.company_description);
            companyWebsite = itemView.findViewById(R.id.company_website);
           // nJobVacancies = itemView.findViewById(R.id.jumlah_lowongan);
        }
    }



    private void sentData( String companyID, String companyName){
        Log.d("CompaniesAdapter", "data is sent");
        Intent toJobs = new Intent(mContext, jobs.class);
        toJobs.putExtra("companyID", companyID);
        toJobs.putExtra("companyName", companyName);
        mContext.startActivity(toJobs);
    }




    private void duplicates(ArrayList<String> job_list){
        for (String item: job_list) {
            if (countMap.containsKey(item))
                countMap.put(item, countMap.get(item) + 1);
            else
                countMap.put(item, 1);
        }
    }
}




