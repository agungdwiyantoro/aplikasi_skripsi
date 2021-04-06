package com.example.agung.PPK_UNY_Mobile.companiesJobs;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agung.PPK_UNY_Mobile.R;
import com.example.agung.PPK_UNY_Mobile.adapters.companiesJobsInActive;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.ref.WeakReference;


public class jobsClosed extends Fragment {

    public final static String ITEMS_COUNT_KEY_MYJOB = "ITEMS_COUNT_KEY_MYJOB";
    public final String TAG = "myJOB";

    private RecyclerView mRecycleView;
    private ProgressBar loading;
    private FirestoreRecyclerAdapter adapter;

    private Activity activity;
    private LinearLayout linearLayout;
    private String companyID;

    View v;

    public jobsClosed() {

    }

    public jobsClosed(String companyID){
        this.companyID = companyID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.jobs, container, false);
        init(v);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        new getDataJob(jobsClosed.this, companyID).execute();


        return v;
    }

    private void init(View v){
        mRecycleView = v.findViewById(R.id.recyclerView);
        linearLayout = v.findViewById(R.id.linear_layout);
        activity = getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "ONSTART");
        if(adapter!=null){
            adapter.startListening();
        }
        else{
            Log.d(TAG, "ADAPTER IS NULL");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "ONSTOP");
        if(adapter!=null){
            adapter.stopListening();
        }
        else{
            Log.d(TAG, "ADAPTER IS NULL");
        }
    }


    private static class getDataJob extends AsyncTask<Void, Void, FirestoreRecyclerAdapter> {
        private WeakReference<jobsClosed> context;
        private String collectionID;

        getDataJob(jobsClosed context, String collectionID){
            this.context = new WeakReference<>(context);
            this.collectionID = collectionID;
        }
        @Override
        protected FirestoreRecyclerAdapter doInBackground(Void... voids) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
           // if(user!=null && user.getProviderData().size()!=1){
                return new companiesJobsInActive( context.get().getActivity(), context.get().linearLayout ).getJobList(collectionID);
         //   }
         //   else {
           //     return new guessCompaniesJobs(context.get().activity).getJobList(collectionID);
         //   }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            context.get().mRecycleView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPostExecute(FirestoreRecyclerAdapter firestoreRecyclerAdapter) {
            super.onPostExecute(firestoreRecyclerAdapter);

            if(firestoreRecyclerAdapter!=null){
                context.get().adapter = firestoreRecyclerAdapter;
                context.get().adapter.notifyDataSetChanged();
                context.get().mRecycleView.setAdapter(context.get().adapter);
                context.get().adapter.startListening();
                context.get().mRecycleView.setVisibility(View.VISIBLE);
            }

        }
    }

}


















































