package com.example.agung.PPK_UNY_Mobile;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agung.PPK_UNY_Mobile.SQLiteDatabase.SQLiteHelper;
import com.example.agung.PPK_UNY_Mobile.adapters.appliedJobsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class applied_jobs extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applied_jobs);

        init();

    }


    void init(){
       // ImageView backButton = findViewById(R.id.back_button);
       // TextView tv_toolbar = findViewById(R.id.tv_toolbar);
       // tv_toolbar.setText(getString(R.string.applied_jobs));
       // backButton.setOnClickListener(view -> finish());
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email;
        if (user.getProviderData().get(1).getProviderId().equals("facebook.com")) {
            email = user.getProviderData().get(1).getEmail();
        } else {
            email = user.getEmail();

        }
        //appliedJobsAdapter apJobsAdapter = new appliedJobsAdapter(new SQLiteHelper(applied_jobs.this,currentFirebaseUser.getDisplayName().replaceAll("\\s",""), sharedPref.getInt(currentFirebaseUser.getEmail(),0)).getAppliedJobs(currentFirebaseUser.getDisplayName().replaceAll("\\s","")));

        appliedJobsAdapter apJobsAdapter = new appliedJobsAdapter(new SQLiteHelper(applied_jobs.this).getAppliedJobs(email), applied_jobs.this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(applied_jobs.this));
        apJobsAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(apJobsAdapter);
    }


}
