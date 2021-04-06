package com.example.agung.PPK_UNY_Mobile.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agung.PPK_UNY_Mobile.R;
import com.example.agung.PPK_UNY_Mobile.model.model_company_list;
import com.example.agung.PPK_UNY_Mobile.model.model_job;
import com.example.agung.PPK_UNY_Mobile.searchAdapters.companiesRecyclerAdapter;
import com.example.agung.PPK_UNY_Mobile.guessAdapters.guessJobsRecyclerAdapter;
import com.example.agung.PPK_UNY_Mobile.searchAdapters.jobsRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class findJob extends Fragment{

    private final static String TAG = "FINDJOB";

    private List<model_job> jobList = new ArrayList<>();
    private List<model_company_list> companyList = new ArrayList<>();

    private companiesRecyclerAdapter companiesRecyclerAdapter;
    private jobsRecyclerAdapter jobsRecyclerAdapter;

    private RecyclerView recycleView;
    private TextView search;
    private CoordinatorLayout coordinatorLayout;

    private ImageView switchCategory;

    private static boolean toggle = false;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(toggle) {
                if (jobsRecyclerAdapter!=null) {
                    jobsRecyclerAdapter.getFilter().filter(search.getText().toString().toLowerCase());
                }
            }
            else{

                if(companiesRecyclerAdapter!= null) {
                    companiesRecyclerAdapter.getFilter().filter(search.getText().toString().toLowerCase());
                }
            }
        }

    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.findjob, container, false);
        Log.d(TAG, "ONCREATE");

//        ((MainActivity)getActivity()).mProgressBar.setVisibility(View.VISIBLE);
        init(v);

        getDataCompanyFromFirebase();

        search.addTextChangedListener(textWatcher);

        search.setOnClickListener(view -> search.addTextChangedListener(textWatcher));

        switchCategory.setOnClickListener(view -> {
            final int search_what;
            search.setText("");
            if(toggle) {
                toggle = false;
                search_what = R.string.search_company;
                getDataCompanyFromFirebase();
            }
            else{
                toggle = true;
                search_what = R.string.search_job;
                getDataJobsFromFirebase();

            }

            search.setHint(search_what);

        });

        return v;
    }


    private void init(View v){
        search = v.findViewById(R.id.searchView);
        recycleView = v.findViewById(R.id.recyclerView);
        switchCategory =  v.findViewById(R.id.switch_category);
        coordinatorLayout = v.findViewById(R.id.coordinatorLayout);
        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
/*
    @Override
    public void onStart() {
        super.onStart();
        search.addTextChangedListener(textWatcher);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "ONRESUME");
        search.addTextChangedListener(textWatcher);
    }
*/
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        search.removeTextChangedListener(textWatcher);
    }


    private void setupRecyclerViewJob(List<model_job> list){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null && user.getProviderData().size()!=1) {
            jobsRecyclerAdapter = new jobsRecyclerAdapter(getActivity(), list, coordinatorLayout);
            jobsRecyclerAdapter.notifyDataSetChanged();
            recycleView.setAdapter(jobsRecyclerAdapter);
        }
        else{
            guessJobsRecyclerAdapter guessJobsRecyclerAdapter = new guessJobsRecyclerAdapter(list, getActivity());
            guessJobsRecyclerAdapter.notifyDataSetChanged();
            recycleView.setAdapter(guessJobsRecyclerAdapter);
        }
    }

    private void setupRecyclerViewCompany(List<model_company_list> list){
//        ((MainActivity)getActivity()).mProgressBar.setVisibility(View.GONE);
        companiesRecyclerAdapter = new companiesRecyclerAdapter(getContext(), list);
        companiesRecyclerAdapter.notifyDataSetChanged();
        recycleView.setAdapter(companiesRecyclerAdapter);
    }

    private void getDataJobsFromFirebase(){
        firebaseFirestore.collection("jobs").orderBy("dateCreated", Query.Direction.DESCENDING).addSnapshotListener((queryDocumentSnapshots, e) -> {
            assert queryDocumentSnapshots != null;
            jobList.clear();
            for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                jobList.add(documentSnapshot.toObject(model_job.class));
            }
            setupRecyclerViewJob(jobList);
        });
    }

    private void getDataCompanyFromFirebase(){
        firebaseFirestore.collection("companies").orderBy("dateCreated", Query.Direction.DESCENDING).addSnapshotListener((queryDocumentSnapshots, e) -> {
            assert queryDocumentSnapshots != null;
            companyList.clear();
            for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                companyList.add(documentSnapshot.toObject(model_company_list.class));
            }
            setupRecyclerViewCompany(companyList);
        });
    }

}
