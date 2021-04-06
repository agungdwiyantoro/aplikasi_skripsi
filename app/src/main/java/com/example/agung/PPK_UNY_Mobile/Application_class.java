package com.example.agung.PPK_UNY_Mobile;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.androidnetworking.AndroidNetworking;
import com.droidnet.DroidNet;
import com.firebase.client.Firebase;

import java.util.Locale;

public class Application_class extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
        AndroidNetworking.initialize(this);
        DroidNet.init(this);
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        DroidNet.getInstance().removeAllInternetConnectivityChangeListeners();
    }

    public static void setAppLocale(Context context, String localCode){
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(localCode.toLowerCase()));
        res.updateConfiguration(conf, dm);
    }

}
