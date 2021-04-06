package com.example.agung.PPK_UNY_Mobile.fragments.profile;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.agung.PPK_UNY_Mobile.BaseActivity;
import com.example.agung.PPK_UNY_Mobile.R;
import com.example.agung.PPK_UNY_Mobile.model.model_about;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class about extends BaseActivity implements View.OnClickListener{


    private ConstraintLayout constraintLayout;
    private List<model_about> about = new ArrayList<>();

    private TextView t1;
    private TextView t2;
    private TextView faxs;
    private TextView google_map_ppk;
    private TextView email_ppk;
    private TextView facebook_ppk;
    private TextView twitter_ppk;
    private TextView instagram_ppk;
    private TextView website;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        faxs = findViewById(R.id.faxs);
        google_map_ppk = findViewById(R.id.gedung_lppmp);
        email_ppk = findViewById(R.id.email_ppk);
        facebook_ppk = findViewById(R.id.fb_ppk);
        twitter_ppk = findViewById(R.id.twitter_ppk);
        instagram_ppk = findViewById(R.id.instagram_ppk);
        website = findViewById(R.id.website);
        constraintLayout = findViewById(R.id.constraint_layout);


        t1.setOnClickListener(this);
        t2.setOnClickListener(this);
        faxs.setOnClickListener(this);
        google_map_ppk.setOnClickListener(this);
        email_ppk.setOnClickListener(this);
        facebook_ppk.setOnClickListener(this);
        twitter_ppk.setOnClickListener(this);
        instagram_ppk.setOnClickListener(this);
        website.setOnClickListener(this);

        getDataAbout();



    }

    private boolean isVerified(Intent mapIntent){
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, PackageManager.MATCH_DEFAULT_ONLY);
        return activities.size() > 0;
    }
    private void call(String phoneNumber){
        Uri number = Uri.parse("tel:"+phoneNumber);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);

        if(isVerified(callIntent)){
            startActivity(callIntent);
        }
    }

    private void call2(String phoneNumber){
        Uri number = Uri.parse("tel:"+phoneNumber);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);

        if(isVerified(callIntent)){
            startActivity(callIntent);
        }
    }

    private void google_map(String myLatitude, String myLongitude, String labelLocation){
        // Uri location = Uri.parse("geo:69GP+XM Caturtunggal, Kabupaten Sleman, Daerah Istimewa Yogyakarta");
        // Or map point based on latitude/longitude
        Uri location = Uri.parse("geo:"+ myLatitude  +"," + myLongitude +"?q=" + labelLocation); // z param is zoom level
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        mapIntent.setPackage("com.google.android.apps.maps");
        if(mapIntent.resolveActivity(getPackageManager()) != null){
            startActivity(mapIntent);
        }else{
            showSnackbar(constraintLayout, getString(R.string.please_install));
        }
    }

    private void email(String email){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
        // The intent does not have a URI, so declare the "text/plain" MIME type
       // emailIntent.setType(HTTP.PLAIN_TEXT_TYPE)
        //emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"jon@example.com"}); // recipients
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Email subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message text");
        //emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"));
        // You can also attach multiple items by passing an ArrayList of Uris

        if(isVerified(emailIntent)){
            startActivity(emailIntent);
        }
    }

    private void instagram(String username){
        Uri uri = Uri.parse("https://www.instagram.com/_u/"+username);
        Intent insta = new Intent(Intent.ACTION_VIEW, uri);
        insta.setPackage("com.instagram.android");

        if(isVerified(insta)){
            startActivity(insta);
        }
        else{
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/"+username)));
        }
    }

    private void facebook(String facebook_username, String facebook_page_kode){
        Intent facebook;
        try {
            getPackageManager().getPackageInfo("com.facebook.katana", 0);
            facebook =  new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/"+ facebook_page_kode));
        } catch (Exception e) {
            facebook = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + facebook_username));
        }
        if(isVerified(facebook)){
            startActivity(facebook);
        }
    }

    private void twitter(String username){
        Intent twitter = null;
        try {
            // get the Twitter app if possible
            this.getPackageManager().getPackageInfo("com.twitter.android", 0);
            twitter = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name="+username));
            twitter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            twitter = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/"+username));
        }

        if(isVerified(twitter)){
            startActivity(twitter);
        }
    }

    private void website(String url){
        Uri uri = Uri.parse(url);
        Intent intentWeb = new Intent(Intent.ACTION_VIEW, uri);
        if(isVerified(intentWeb)){
            startActivity(intentWeb);
        }
    }

    @Override
    public void onClick(View view) {

        if (view.getId()==R.id.t1) {
            call(about.get(0).getPhonenumber_one());
        }


        if (view.getId()==R.id.t2) {
            call(about.get(0).getPhonenumber_two());
        }


        if (view.getId()==R.id.gedung_lppmp) {
            google_map(about.get(0).getLatitude(), about.get(0).getLongitude(), about.get(0).getLocation());
        }


        if (view.getId()==R.id.email_ppk) {
            email(about.get(0).getEmail());
        }


        if (view.getId()==R.id.fb_ppk) {
            facebook(about.get(0).getFacebook_username(), about.get(0).getFacebook_page_kode());
        }


        if (view.getId()==R.id.twitter_ppk) {
            twitter(about.get(0).getTwitter_username());
        }


        if (view.getId()==R.id.instagram_ppk) {
            instagram(about.get(0).getInst_username());
        }

        if(view.getId()==R.id.website){
            website(about.get(0).getWebsite_url());
        }

    }

    private void getDataAbout(){
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        DocumentReference dr = fs.collection("about").document("data");
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                about.add(task.getResult().toObject(model_about.class));
                Log.d("latitude", about.get(0).getLatitude());
                google_map_ppk.setText(about.get(0).getAddress());
                t1.setText(about.get(0).getPhonenumber_one());
                t2.setText(about.get(0).getPhonenumber_two());
                email_ppk.setText(about.get(0).getEmail());
                facebook_ppk.setText(about.get(0).getFacebook_username());
                twitter_ppk.setText(about.get(0).getTwitter_username());
                instagram_ppk.setText(about.get(0).getInst_username());
                website.setText(about.get(0).getWebsite_url());
                faxs.setText(about.get(0).getFax_number());
            }
        });
    }
}
