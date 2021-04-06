package com.example.agung.PPK_UNY_Mobile.signIn;

import android.app.Activity;
import android.util.Log;

import com.example.agung.PPK_UNY_Mobile.MainActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.agung.PPK_UNY_Mobile.BaseActivity.toActivityClear;


public class facebookSignIn {

    private static String TAG = "facebookSignIn";
    private Activity activity;


    private CallbackManager callbackManager;

    public facebookSignIn(Activity activity){
        this.activity = activity;
    }

    public void facebookLoginManager() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {

                        //showSnackbar("Login Cancelled");

                    }


                    @Override

                    public void onError(FacebookException exception) {
                        // showSnackbar("Login Error");
                    }

                });
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        toActivityClear(activity, MainActivity.class);
                        activity.finish();
                    }
                });
    }


    public void facebookSignOut(Runnable run, Runnable run2, Runnable run3){
        //LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_ONLY);
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // user already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, graphResponse -> {
            LoginManager.getInstance().logOut();
        }).executeAsync();

        FirebaseAuth.getInstance().signOut();
        run.run();
        run2.run();
        run3.run();
    }


    public CallbackManager getCallbackManager() {
        return callbackManager;
    }


}
