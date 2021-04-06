package com.example.agung.PPK_UNY_Mobile.companiesJobs;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.agung.PPK_UNY_Mobile.BaseActivity;
import com.example.agung.PPK_UNY_Mobile.DemoCollectionPagerAdapter;
import com.example.agung.PPK_UNY_Mobile.R;
import com.google.android.material.tabs.TabLayout;


public class jobsCompaniesJob extends BaseActivity {
    DemoCollectionPagerAdapter demoCollectionPagerAdapter;
    ViewPager viewPager;
    String companyID, companyName, jobID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_job_new);

        TextView tvToolbar = findViewById(R.id.tv_toolbar);
        ImageView backButton = findViewById(R.id.back_button);

        tvToolbar.setText(getIntent().getStringExtra("companyName"));

        backButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               jobsCompaniesJob.this.finish();
           }
       });

        companyID = getIntent().getStringExtra("companyID");
  //      jobID = getIntent().getStringExtra("jobID");

       Log.d("jobIDlul", companyID);


        demoCollectionPagerAdapter = new DemoCollectionPagerAdapter(getSupportFragmentManager(), 3);
        viewPager = findViewById(R.id.pager);
        demoCollectionPagerAdapter.addFragment(new jobs(companyID), getResources().getString(R.string.lowongan_buka));
        demoCollectionPagerAdapter.addFragment(new jobsClosed(companyID),getResources().getString(R.string.lowongan_tutup));
        demoCollectionPagerAdapter.addFragment(new panggilanTes(companyID),getResources().getString(R.string.panggilan_tes));
        viewPager.setAdapter(demoCollectionPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);


        tabLayout.setupWithViewPager(viewPager);
    }


}
















































