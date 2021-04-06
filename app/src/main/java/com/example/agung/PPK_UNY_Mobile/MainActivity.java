package com.example.agung.PPK_UNY_Mobile;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.agung.PPK_UNY_Mobile.SQLiteDatabase.SQLiteHelper;
import com.example.agung.PPK_UNY_Mobile.firebase.FirebaseSaveToken;
import com.example.agung.PPK_UNY_Mobile.fragments.advertisement;
import com.example.agung.PPK_UNY_Mobile.fragments.findJob;
import com.example.agung.PPK_UNY_Mobile.fragments.profile.profile;
import com.example.agung.PPK_UNY_Mobile.fragments.myJob.myJob;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private final String TAG = this.getClass().getName();


    public ViewPager viewPager;
    public PagerAdapter pagerAdapter;
    public ProgressBar mProgressBar;
    public static SQLiteHelper sqLiteHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            setAppLocale(getSubscription(getString(R.string.key_language), "en"));
            toActivity(MainActivity.this, login.class);
            finish();
        } else {
            setAppLocale(getSubscription(getString(R.string.key_language), "en"));
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user.getProviderData().size()!=1) {
                String email;
                if (user.getProviderData().get(1).getProviderId().equals("facebook.com")) {
                    email = user.getProviderData().get(1).getEmail();
                } else {
                    email = user.getEmail();
                }

                FirebaseSaveToken.saveToken(email);
            }

            String defaulValue = "null";
            String [] topics = getResources().getStringArray(R.array.topics);
            for (String s : topics) {
                String topic = getSubscription(s, s);
                Log.d(TAG, "topic is " + topic);
                if(!topic.equals(defaulValue)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(topic);
                }
            }

            initViewPager();
        }

    }

    private void initViewPager(){
        setContentView(R.layout.activity_main);

        sqLiteHelper = new SQLiteHelper(MainActivity.this);
        viewPager = findViewById(R.id.viewPager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());


        View headerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.custom_tab, null, false);

        pagerAdapter.addFragment(new findJob());
        //pagerAdapter.addFragment(new myJob());
        pagerAdapter.addFragment(new myJob());
        pagerAdapter.addFragment(new advertisement());
        pagerAdapter.addFragment(new profile());
        viewPager.addOnPageChangeListener(pagerAdapter);

        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        mProgressBar = findViewById(R.id.progressbar);

        TextView findJob =  headerView.findViewById(R.id.find_job);
        TextView myJob =  headerView.findViewById(R.id.my_job);
        TextView information =  headerView.findViewById(R.id.information);
        TextView myProfile = headerView.findViewById(R.id.my_profile);
        tabLayout.getTabAt(0).setCustomView(findJob);
        tabLayout.getTabAt(1).setCustomView(myJob);
        tabLayout.getTabAt(2).setCustomView(information);
        tabLayout.getTabAt(3).setCustomView(myProfile);
    }

    public class PagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

        List<Fragment> listFragment = new ArrayList<>();
        List<String> listTitle = new ArrayList<>();

        void addFragment(Fragment fragment){
            listFragment.add(fragment);
        }

        void addFragment(Fragment fragment, String title){
            listFragment.add(fragment);
            listTitle.add(title);
        }

        PagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {
            return listFragment.get(position);
        }

      //  @Override
      //  public int getItemPosition(@NonNull Object object) {
    //        return POSITION_NONE;
     //   }

        @Override
        public int getCount() {
            return listFragment.size();
        }

        public void removeFragment(Fragment fragment, int i){
            listFragment.remove(i);
            listTitle.remove(i);
        }

        public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }

}





