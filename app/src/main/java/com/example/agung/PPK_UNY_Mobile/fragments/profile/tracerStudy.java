package com.example.agung.PPK_UNY_Mobile.fragments.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.agung.PPK_UNY_Mobile.BaseActivity;
import com.example.agung.PPK_UNY_Mobile.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class tracerStudy extends BaseActivity {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference t_s = firebaseFirestore.
            collection("tracer_study");
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracer_study);
        constraintLayout = findViewById(R.id.constraint_layout);

        getLink();
    }

    public tracerStudy(){

    }


    private void getLink(){
        t_s.addSnapshotListener((value, error) -> {
                    if(value!=null) {

                            String link = value.getDocuments().get(0).get("link").toString();
                            Log.d("link tracer", "fu " + link);
                        webSettings(link);

                    }else{
                        showSnackbarAddCallback(constraintLayout, getString(R.string.snack_tracer_study), tracerStudy.this);
                    }
                });

                /*
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Log.d("link tracer", "fu " + task.getResult().getString("link"));
                        if(task.getResult().getString("link")!=null) {
                            webSettings(task.getResult().getString("link"));
                        }
                        else{
                            showSnackbarAddCallback(constraintLayout, getString(R.string.snack_tracer_study), tracerStudy.this);
                        }
                    }

                });

                 */
    }


    void webSettings(String url){
        WebView web_tracer_study = findViewById(R.id.tracer_study);

        WebSettings webSettings = web_tracer_study.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDomStorageEnabled(true);

        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        web_tracer_study.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        web_tracer_study.setWebViewClient(new WebViewClient());
        web_tracer_study.loadUrl(url);
    }

}
