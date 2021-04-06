package com.example.agung.PPK_UNY_Mobile.fragments.myJob;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agung.PPK_UNY_Mobile.R;
import com.example.agung.PPK_UNY_Mobile.adapters.myJobsAdapter;
import com.example.agung.PPK_UNY_Mobile.login;
import com.example.agung.PPK_UNY_Mobile.model.model_job;
import com.example.agung.PPK_UNY_Mobile.signUp;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class myJobSaved extends Fragment implements View.OnClickListener {

    public final String TAG = "myJOB";

    private RecyclerView mRecycleView;
    private FirestoreRecyclerAdapter adapter;
    private myJobsAdapter mJobsAdapter;
    private LinearLayout linearLayout;
    private ConstraintLayout constraintLayout;
    private ProgressDialog mProgressDialog;
    private View v;

    private FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();



    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private List<model_job> listModelMyJob = new ArrayList<>();

    public myJobSaved() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null && user.getProviderData().size()!=1) {
            v =  inflater.inflate(R.layout.myjob, container, false);
            init();
            //new getDataJob(myJob.this).execute();
            mRecycleView.setVisibility(View.VISIBLE);
            constraintLayout.setVisibility(View.GONE);
           // firebaseFirestore.collection("jobs")
                   // .addSnapshotListener((queryDocumentSnapshots, e) ->
            getDataFromFirebase();
                  //  );

           // firebaseFirestore.collection("jobs").addSnapshotListener((value, error) -> {

           // });
        }
        else{
            v =  inflater.inflate(R.layout.my_job_guess, container, false);
            init2();
        }

        return v;
    }

    private void update(LayoutInflater inflater, ViewGroup container){

    }

    private void init(){
        mRecycleView = v.findViewById(R.id.recyclerView);
        linearLayout = v.findViewById(R.id.linear_layout);

        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        constraintLayout = v.findViewById(R.id.constraint_layout);
   }

   private void init2(){
       TextView daftar = v.findViewById(R.id.daftar);
       TextView masuk = v.findViewById(R.id.masuk);

       daftar.setOnClickListener(this);
       masuk.setOnClickListener(this);
   }
/*
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "ONSTART");
        if(adapter!=null ){
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

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "ONRESUME");
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }
*/
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

    private void setupRecyclerViewMyJob(List<model_job> list){
        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
       // if(user!=null && user.getProviderData().size()!=1) {
            com.example.agung.PPK_UNY_Mobile.adapters.myJobsAdapter myJobsAdapter = new myJobsAdapter(getActivity(), list, linearLayout);
            myJobsAdapter.notifyDataSetChanged();
            mRecycleView.setAdapter(myJobsAdapter);
        //}
    }

    private void getDataFromFirebase(){
        //if(user != null && user.getProviderData().size()!=1) {
            firebaseFirestore.collection("users_jobs").
                    document(user.getUid()).
                    collection("jobs").orderBy("dateCreated", Query.Direction.DESCENDING).addSnapshotListener((queryDocumentSnapshots, e) -> {

                //int count = 0;
                assert queryDocumentSnapshots != null;
                listModelMyJob.clear();
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    listModelMyJob.add(documentSnapshot.toObject(model_job.class));
                    //Log.d(TAG, "in" + listModelMyJob.get(count).getJobName());
                    //count++;
                }
                setupRecyclerViewMyJob(listModelMyJob);
            });
        //}
    }

    /*

       Log.d("KNTLOLE", "jobName" + documentSnapshot.getString("jobName"));


    private static class getDataJob extends AsyncTask<Void, Void, FirestoreRecyclerAdapter> {
        private WeakReference<myJob> context;
        getDataJob(myJob context){
            this.context = new WeakReference<>(context);
        }
        @Override
        protected FirestoreRecyclerAdapter doInBackground(Void... voids) {
           if(FirebaseAuth.getInstance().getCurrentUser()!=null){
               return new myJobsAdapter(context.get().getActivity(), context.get().linearLayout).getJobList();
            }
           else{
               return null;
           }
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
                context.get().constraintLayout.setVisibility(View.GONE);
            }
        }
    }

*/

}


















































