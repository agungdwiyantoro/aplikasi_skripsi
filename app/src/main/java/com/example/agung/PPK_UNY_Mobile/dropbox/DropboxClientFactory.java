package com.example.agung.PPK_UNY_Mobile.dropbox;

import android.util.Log;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.example.agung.PPK_UNY_Mobile.httpRequest.OkHttp3Requestor;


/**
 * Singleton instance of {@link DbxClientV2} and friends
 */
public class DropboxClientFactory {

    private static DbxClientV2 sDbxClient;


    public static void init(String accessToken) {
        if (sDbxClient == null) {
            DbxRequestConfig requestConfig = DbxRequestConfig.newBuilder("examples-v2-demo")
                .withHttpRequestor(OkHttp3Requestor.INSTANCE)
                .build();

            sDbxClient = new DbxClientV2(requestConfig, accessToken);

            Log.d("DropboxClientFactory", "TENTANG KITA");
        }
    }

    public static DbxClientV2 getClient() {
        if (sDbxClient == null) {
            Log.d("DropboxClientFactory", "TENTANG KITA 2 ");
            throw new IllegalStateException("Client not initialized.");
        }
        return sDbxClient;
    }

    public static void clearClient() {
        sDbxClient = null;
    }
}
