package com.example.agung.PPK_UNY_Mobile.fragments.myJob;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agung.PPK_UNY_Mobile.R;
import com.example.agung.PPK_UNY_Mobile.adapters.appliedJobsAdapter;
import com.example.agung.PPK_UNY_Mobile.login;
import com.example.agung.PPK_UNY_Mobile.model.model_appliedJobs;
import com.example.agung.PPK_UNY_Mobile.signUp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class applied_jobs_new extends Fragment implements View.OnClickListener{

    private View view;
    private appliedJobsAdapter apJobsAdapter;
    private RecyclerView mRecyclerView;
    private FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null && user.getProviderData().size()!=1) {
            view = inflater.inflate(R.layout.applied_jobs, container, false);
            init();
        }
        else{
            view = inflater.inflate(R.layout.applied_jobs_guess, container, false);
            init2();
        }
        return view;
    }


    void init(){
        mRecyclerView = view.findViewById(R.id.recyclerView);
        //appliedJobsAdapter apJobsAdapter = new appliedJobsAdapter(new SQLiteHelper(applied_jobs.this,currentFirebaseUser.getDisplayName().replaceAll("\\s",""), sharedPref.getInt(currentFirebaseUser.getEmail(),0)).getAppliedJobs(currentFirebaseUser.getDisplayName().replaceAll("\\s","")));
        getDataAppliedJobs();


    }




    private void init2(){
        TextView daftar = view.findViewById(R.id.daftar);
        TextView masuk = view.findViewById(R.id.masuk);

        daftar.setOnClickListener(this);
        masuk.setOnClickListener(this);
    }


    private void getDataAppliedJobs(){
        List<model_appliedJobs> apJobList = new ArrayList<>();
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        CollectionReference applied_jobs =  fs.collection("users_jobs")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .collection("applied");
        applied_jobs
                .addSnapshotListener((value, error) -> {
                    assert value != null;
                    for(DocumentSnapshot snap : value){
                        apJobList.add(snap.toObject(model_appliedJobs.class));
                    }

                    apJobsAdapter = new appliedJobsAdapter(apJobList, getActivity());
                    mRecyclerView.setHasFixedSize(true);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    apJobsAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(apJobsAdapter);
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.daftar :
                toSignUpActivity();
                break;
            case R.id.masuk :
                toLoginActivity();
                break;
        }
    }

    private void toLoginActivity(){
        Intent intent = new Intent(getActivity(), login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void toSignUpActivity(){
        Intent intent = new Intent(getActivity(), signUp.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
