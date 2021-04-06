package com.example.agung.PPK_UNY_Mobile.guessAdapters;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.agung.PPK_UNY_Mobile.BaseActivity;
import com.example.agung.PPK_UNY_Mobile.R;
import com.example.agung.PPK_UNY_Mobile.login;
import com.example.agung.PPK_UNY_Mobile.signUp;

public class guessMyJob extends BaseActivity implements View.OnClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_job_guess);

        init();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.daftar :
                toActivity(guessMyJob.this, login.class);
                finish();
                break;
            case R.id.masuk :
                toActivity(guessMyJob.this, signUp.class);
                break;
        }

    }

    void init(){
        TextView daftar = findViewById(R.id.daftar);
        TextView masuk = findViewById(R.id.masuk);

        daftar.setOnClickListener(this);
        masuk.setOnClickListener(this);
    }

}
