package com.example.agung.PPK_UNY_Mobile.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.agung.PPK_UNY_Mobile.R;
import com.example.agung.PPK_UNY_Mobile.adBannerAdapter;
import com.example.agung.PPK_UNY_Mobile.model.model_advertisement;
import com.example.agung.PPK_UNY_Mobile.searchAdapters.advertisementRecyclerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;



public class advertisement extends Fragment {


    private final String TAG = "advertisement";
    private EditText searchView;
    private PopupMenu.OnMenuItemClickListener onMenuItemClickListener;
    private advertisementRecyclerAdapter recyclerAdapterAdvertisement;
    private RecyclerView recyclerView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private List<model_advertisement> listModelAd = new ArrayList<>();
    private List<String> url = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.adverstisement, container, false);
        init(v);
        setHasOptionsMenu(true);
        getDataFromFirebase();
        getBanner(getActivity());
        return v;
    }

    private void init(View v){
        searchView = v.findViewById(R.id.searchView);
        viewPager = v.findViewById(R.id.pager);
        ImageView imageView = v.findViewById(R.id.imageView);
        tabLayout = v.findViewById(R.id.tab_layout);
        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        tabLayout.setupWithViewPager(viewPager, true);

        onMenuItemClickListener = menuItem -> {

            switch (menuItem.getItemId()) {
                case R.id.none:
                    recyclerAdapterAdvertisement.getFilter().filter("");
                    break;
                case R.id.berita:
                    recyclerAdapterAdvertisement.getFilter().filter("Berita");
                    break;
                case R.id.job_seeker:
                    recyclerAdapterAdvertisement.getFilter().filter("Job Seeker");
                    break;
                case R.id.tips_karir:
                    recyclerAdapterAdvertisement.getFilter().filter("Tips Karir");
                    break;
                case R.id.panggilan_test:
                    recyclerAdapterAdvertisement.getFilter().filter("Panggilan Test");
                    break;
            }

            return false;
        };

        imageView.setOnClickListener(this::showPopup);

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (recyclerAdapterAdvertisement != null) {
                    recyclerAdapterAdvertisement.getFilter().filter(searchView.getText().toString().toLowerCase());
                }
            }
        });
    }


    private void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_advertisement, popup.getMenu());
        popup.setOnMenuItemClickListener(onMenuItemClickListener);
        popup.show();
    }

    private void setupRecyclerView(List<model_advertisement> list){
        recyclerAdapterAdvertisement = new advertisementRecyclerAdapter(list);
        recyclerView.setAdapter(recyclerAdapterAdvertisement);
    }


    private void getDataFromFirebase(){
        firebaseFirestore.collection("Informasi").orderBy("dateCreated", Query.Direction.DESCENDING).addSnapshotListener((queryDocumentSnapshots, e) -> {
            int count = 0;
            assert queryDocumentSnapshots != null;
            listModelAd.clear();
            for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                listModelAd.add(documentSnapshot.toObject(model_advertisement.class));
                Log.d(TAG, "in" + listModelAd.get(count).getInformasi_name());
                count++;
            }
            setupRecyclerView(listModelAd);
        });
    }

   private void getBanner(Activity activity) {
       url.clear();
        firebaseFirestore.collection("banner").orderBy("dateCreated", Query.Direction.DESCENDING)
               .addSnapshotListener((queryDocumentSnapshots, e) -> {
                   url.clear();
                   for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                       url.add(documentSnapshot.getString("link"));

                   }
                   Log.d(TAG, "list " + url.get(0));
                   Log.d(TAG, "list " + url.get(1));

                   new adBannerAdapter(activity, url, viewPager).notifyDataSetChanged();
                   viewPager.setAdapter(new adBannerAdapter(activity, url, viewPager));
                 //  url.clear();
               });

   }

}

