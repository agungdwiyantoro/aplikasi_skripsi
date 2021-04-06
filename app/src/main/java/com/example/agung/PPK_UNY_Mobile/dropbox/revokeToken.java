package com.example.agung.PPK_UNY_Mobile.dropbox;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.dropbox.core.DbxException;
import com.dropbox.core.android.Auth;
import com.dropbox.core.android.AuthActivity;
import com.example.agung.PPK_UNY_Mobile.R;

import java.lang.ref.WeakReference;

public class revokeToken extends AsyncTask<Void, Void, Void> {

    private SharedPreferences prefs;
    private WeakReference<Context> context;

    public revokeToken(SharedPreferences prefs, Context context){
        this.prefs = prefs;
        this.context = new WeakReference<>(context);
    }
    @Override
    protected Void doInBackground(Void... voids) {
        revokeTheToken();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Auth.startOAuth2Authentication(context.get(), context.get().getString(R.string.app_key));
    }

    private void revokeTheToken(){
        try {
            DropboxClientFactory.getClient().auth().tokenRevoke();
            prefs.edit().clear().apply();
            String accessToken = prefs.getString("access-token", null);
            DropboxClientFactory.clearClient();
            AuthActivity.result =null;
        } catch (DbxException e) {
            e.printStackTrace();
        }
    }


}
