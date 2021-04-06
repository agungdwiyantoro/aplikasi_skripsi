package com.example.agung.PPK_UNY_Mobile;


import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.agung.PPK_UNY_Mobile.alertDialogs.alertDialogs.bannerFullScreen;

public class adBannerAdapter extends PagerAdapter {

    private static final String TAG = "CUSTOM PAGER ADAPTER";
    private LayoutInflater mLayoutInflater;
    private ViewPager mViewPager;
    private List<String> mResources;
    private Activity context;


    public adBannerAdapter(Activity context, List<String> resources, ViewPager viewPager) {
        mResources = resources;
        mViewPager = viewPager;
        this.context = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mResources.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        Log.d(TAG,
                "instantiateItem() called with: " + "container = [" + container + "], position = [" + position + "]");

        View itemView = mLayoutInflater.inflate(R.layout.image_holder, container, false);
        final ImageView ivPhoto =  itemView.findViewById(R.id.iv_image);
        ivPhoto.setScaleType(ImageView.ScaleType.FIT_XY);

        Log.d(TAG, "load in gallery:" + mResources.get(position)+ "#end");

        Picasso.get()
                .load(mResources.get(position).trim())
                .into(ivPhoto);

        final Handler handler = new Handler();
        final Runnable update = () -> {
            Log.d(TAG, "cur item " + mViewPager.getCurrentItem() + ", " + (mResources.size()-1));
            if(mViewPager.getCurrentItem()==mResources.size()-1){
                mViewPager.setCurrentItem(0,false);
            }else{
                mViewPager.setCurrentItem( mViewPager.getCurrentItem()+1);
            }
        };



        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            Boolean first = true;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (first && positionOffset == 0 && positionOffsetPixels == 0){
                    onPageSelected(0);
                    first = false;
                }
            }


            @Override

            public void onPageSelected(int position) {
                // super.onPageSelected(position);
                handler.removeCallbacks(update);
                handler.postDelayed(update, 8000);
            }
        });
     //   }


        container.addView(itemView);

        ivPhoto.setOnClickListener(view -> bannerFullScreen(context, mResources.get(position).trim()));

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Log.d(TAG, "destroyItem() called with: " + "container = [" + container + "], position = [" + position
                + "], object = [" + object + "]");
        container.removeView((FrameLayout) object);
    }


}