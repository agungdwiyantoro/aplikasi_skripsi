package com.example.agung.PPK_UNY_Mobile.signIn;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.agung.PPK_UNY_Mobile.MainActivity;
import com.example.agung.PPK_UNY_Mobile.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import static com.example.agung.PPK_UNY_Mobile.BaseActivity.toActivityClear;

public class googleSignIn {


    private final String TAG = "googleSignIn";
    public final static int RC_GOOGLE_SIGN_IN = 9001;

    private Activity activity;


    private GoogleSignInClient googleSignInClient;

    public googleSignIn(Activity activity){
        this.activity = activity;
    }

        public void configureGoogleSignIn() {
            Log.d(TAG, "configureGoogleSignIn");
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(activity.getString(R.string.web_id_cliet))
                    .requestEmail()
                    .build();

            googleSignInClient = GoogleSignIn.getClient(activity, gso);
        }

        private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
            Log.d(TAG, "method firebaseAuthWithGoogle");
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.w(TAG, "signInWithCredential :success" + task.getException());

                                toActivityClear(activity, MainActivity.class);
                                activity.finish();
                            }                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "signInWithCredential :fail" + e.getMessage());
                }
            });
        }


        public void googleSignInActivityResult(Intent data) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
                if (googleSignInAccount != null) {
                    firebaseAuthWithGoogle(googleSignInAccount);
                }
            } catch(ApiException e)
            {
                e.printStackTrace();
            }
        }




    public void googleSignInMethod() {
        Log.d(TAG, "method signIn");
        Intent signInIntent = googleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }


    public void googleSignOut(Runnable run, Runnable run2, Runnable run3) {
        googleSignInClient.signOut().addOnCompleteListener(activity,
                task -> {
                    FirebaseAuth.getInstance().signOut();
                    run.run();
                    run2.run();
                    run3.run();
                });
    }
}
