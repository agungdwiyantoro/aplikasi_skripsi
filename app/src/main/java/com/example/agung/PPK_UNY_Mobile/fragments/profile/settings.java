package com.example.agung.PPK_UNY_Mobile.fragments.profile;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.FileProvider;

import com.example.agung.PPK_UNY_Mobile.BaseActivity;
import com.example.agung.PPK_UNY_Mobile.MainActivity;
import com.example.agung.PPK_UNY_Mobile.R;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Locale;

public class settings extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private SwitchCompat switch_button;
    private ToggleButton all, berita, jobSeeker, tipsKarir, panggilanTes;
    private RadioButton indonesia, english;
    private TextView cache_size;


    private final String TAG = "notification";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        init();


    }

    private void init(){
        ImageView backButton = findViewById(R.id.back_button);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        tv_toolbar.setText(getString(R.string.settings));

        switch_button = findViewById(R.id.switch_button);

        all = findViewById(R.id.rb_all);
        berita = findViewById(R.id.rb_berita);
        jobSeeker = findViewById(R.id.rb_job_seeker);
        tipsKarir = findViewById(R.id.rb_job_tips_karir);
        panggilanTes = findViewById(R.id.rb_panggilan_test);

        indonesia = findViewById(R.id.bahasa_indonesia);
        english = findViewById(R.id.english);
        Button clearCache = findViewById(R.id.bt_clear_cache);
        cache_size = findViewById(R.id.tv_cache_size_var);
        cache_size.setText(humanReadableByteCountBin(getCacheSize(save_directory())));

        backButton.setOnClickListener(this);

        switch_button.setOnClickListener(this);

        all.setOnCheckedChangeListener(this);
        berita.setOnCheckedChangeListener(this);
        jobSeeker.setOnCheckedChangeListener(this);
        tipsKarir.setOnCheckedChangeListener(this);
        panggilanTes.setOnCheckedChangeListener(this);

        indonesia.setOnClickListener(this);
        english.setOnClickListener(this);
        clearCache.setOnClickListener(this);

        setCondition();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.switch_button :
                if (switch_button.isChecked()) {
                    FirebaseMessaging.getInstance().subscribeToTopic("job");
                    saveSubscription("job", "job");
                }
                else{
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("job");
                    saveSubscription("job", "null");
                }
                break;

            case R.id.back_button :
                finish();
                break;

            case R.id.bahasa_indonesia :
                if (indonesia.isChecked()){
                    saveSubscription(getString(R.string.key_language), "id");
                    setAppLocale(getSubscription(getString(R.string.key_language), "en"));
                    toActivityClear(settings.this, MainActivity.class);
                }
                break;

            case R.id.english :
                if(english.isChecked()){
                    saveSubscription(getString(R.string.key_language), "en");
                    setAppLocale(getSubscription(getString(R.string.key_language), "en"));
                    toActivityClear(settings.this, MainActivity.class);
                }
                break;

            case R.id.bt_clear_cache :
                clearCache(save_directory());
                break;
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.rb_all :
                if(b){
                    berita.setChecked(true);
                    jobSeeker.setChecked(true);
                    tipsKarir.setChecked(true);
                    panggilanTes.setChecked(true);
                    all.setChecked(true);
                    FirebaseMessaging.getInstance().subscribeToTopic("Berita");
                    FirebaseMessaging.getInstance().subscribeToTopic("JobSeeker");
                    FirebaseMessaging.getInstance().subscribeToTopic("TipsKarir");
                    FirebaseMessaging.getInstance().subscribeToTopic("PanggilanTes");
                    saveSubscription("Berita", "Berita");
                    saveSubscription("JobSeeker", "JobSeeker");
                    saveSubscription("TipsKarir", "TipsKarir");
                    saveSubscription("PanggilanTes", "PanggilanTes");
                }
                else {
                    berita.setChecked(false);
                    jobSeeker.setChecked(false);
                    tipsKarir.setChecked(false);
                    panggilanTes.setChecked(false);
                    all.setChecked(false);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Berita");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("JobSeeker");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("TipsKarir");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("PanggilanTes");
                    saveSubscription("Berita","null");
                    saveSubscription("JobSeeker","null");
                    saveSubscription("TipsKarir","null");
                    saveSubscription("PanggilanTes","null");
                }

                break;

            case R.id.rb_berita:
                if (b) {
                    FirebaseMessaging.getInstance().subscribeToTopic("Berita");
                    saveSubscription("Berita", "Berita");
                }
                else{
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Berita");
                    saveSubscription("Berita", "null");
                }

                break;

            case R.id.rb_job_seeker :
                if (b) {
                    FirebaseMessaging.getInstance().subscribeToTopic("JobSeeker");
                    saveSubscription("JobSeeker", "JobSeeker");
                }
                else{
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("JobSeeker");
                    saveSubscription("JobSeeker", "null");
                }
                break;

            case R.id.rb_job_tips_karir :
                if (b) {
                    FirebaseMessaging.getInstance().subscribeToTopic("TipsKarir");

                    saveSubscription("TipsKarir", "TipsKarir");
                }
                else{
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("TipsKarir");
                    saveSubscription("TipsKarir", "null");
                }
                break;

            case R.id.rb_panggilan_test :
                if (panggilanTes.isChecked()) {
                    FirebaseMessaging.getInstance().subscribeToTopic("PanggilanTes");
                    saveSubscription("PanggilanTes", "PanggilanTes");
                }
                else{
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("PanggilanTes");
                    saveSubscription("PanggilanTes", "null");
                }
                break;
        }
    }

    private void setCondition(){
        String [] topics = getResources().getStringArray(R.array.topics);
        String defaultValue = "null";
        int x = 0;
        for (String s : topics) {
            String topic = getSubscription(s, s);
            if (topic.equals(defaultValue)) {

                x++;
                switch (s) {
                    case "Berita":
                        berita.setChecked(false);
                        break;
                    case "JobSeeker":
                        jobSeeker.setChecked(false);
                        break;
                    case "TipsKarir":
                        tipsKarir.setChecked(false);
                        break;
                    case "PanggilanTes":
                        panggilanTes.setChecked(false);
                        break;

                    case "job":
                        switch_button.setChecked(false);
                        break;
                }
            }

            if(x==4){
                all.setChecked(false);
            }
        }

        String en_in = getSubscription(getString(R.string.key_language), "en");
        if(en_in.equals("id")){
            indonesia.setChecked(true);
        }
        else{
            english.setChecked(true);
        }
    }

    private void clearCache(File fileOrDirectory){
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                clearCache(child);
                Log.d(TAG,"file is " + child.getName());
            }
        }
        fileOrDirectory.delete();
        cache_size.setText(humanReadableByteCountBin(getCacheSize(save_directory())));
    }

    private File fileDirectory(){
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
    }

    private File save_directory(){
        Uri contentUril = FileProvider.getUriForFile(settings.this, "com.example.agung.aplikasi_skripsi.provider", fileDirectory());
        return new File("sdcard/" + contentUril.toString());
    }

    private long getCacheSize(File fileOrDirectory){
        long total = 0;
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                total += getCacheSize(child);
                Log.d(TAG,"Size is " + total);
            }

        }
        else{
            total = fileOrDirectory.length();
        }

        return total;
    }

    public static String humanReadableByteCountBin(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format(Locale.US, "%.1f %ciB", value / 1024.0, ci.current());
    }
}
