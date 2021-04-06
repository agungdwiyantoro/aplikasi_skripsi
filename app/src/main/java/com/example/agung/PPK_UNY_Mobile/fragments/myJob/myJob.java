package com.example.agung.PPK_UNY_Mobile.fragments.myJob;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.agung.PPK_UNY_Mobile.DemoCollectionPagerAdapter;
import com.example.agung.PPK_UNY_Mobile.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class myJob extends Fragment {
    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    DemoCollectionPagerAdapter demoCollectionPagerAdapter;
    ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_job_new, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        TextView tvToolbar = view.findViewById(R.id.tv_toolbar);
        ImageView backButton = view.findViewById(R.id.back_button);

        tvToolbar.setText(getString(R.string.myJobs));
        backButton.setVisibility(View.GONE);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        
        demoCollectionPagerAdapter = new DemoCollectionPagerAdapter(getChildFragmentManager(), 2);

        viewPager = view.findViewById(R.id.pager);
        demoCollectionPagerAdapter.addFragment(new myJobSaved(), getResources().getString(R.string.disimpan));
        demoCollectionPagerAdapter.addFragment(new applied_jobs_new(), getResources().getString(R.string.applied));

        viewPager.setAdapter(demoCollectionPagerAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }
}

