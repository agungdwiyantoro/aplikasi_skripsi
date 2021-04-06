package com.example.agung.PPK_UNY_Mobile;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.agung.PPK_UNY_Mobile.signIn.facebookSignIn;
import com.example.agung.PPK_UNY_Mobile.signIn.googleSignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.example.agung.PPK_UNY_Mobile.alertDialogs.alertDialogs.resetPassword;
import static com.example.agung.PPK_UNY_Mobile.signIn.googleSignIn.RC_GOOGLE_SIGN_IN;

public class login extends BaseActivity implements View.OnClickListener {

    private final String TAG = login.class.getName();

    private EditText emailAddress, password;
    private ConstraintLayout constraintLayout;
    private List<String> permissionNeeds;

    private googleSignIn gSignIn;
    private facebookSignIn fSignIn;

    private static boolean networkStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        init();
    }

    void init() {
        ImageView signInButton = findViewById(R.id.sign_in_google);
        ImageView signInFacebook = findViewById(R.id.sign_in_facebook);
        Button signIn = findViewById(R.id.login);
        ImageView back = findViewById(R.id.back_button);
        TextView loginGuess = findViewById(R.id.skip);
        TextView signUp = findViewById(R.id.create_account);
        TextView forgotPassword = findViewById(R.id.tv_forgot_password);
        emailAddress = findViewById(R.id.email);
        password = findViewById(R.id.password);

        signInButton.setOnClickListener(this);
        signInFacebook.setOnClickListener(this);
        signIn.setOnClickListener(this);
        loginGuess.setOnClickListener(this);
        signUp.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        back.setOnClickListener(this);

        permissionNeeds = Arrays.asList("user_photos", "email");

        constraintLayout = findViewById(R.id.constraint_layout);

        gSignIn = new googleSignIn(login.this);
       // fSignIn = new facebookSignIn(login.this);

      //  fSignIn.facebookLoginManager();
        gSignIn.configureGoogleSignIn();

    }


    @Override
    public void onClick(View view) {
        showProgressDialog();
        switch (view.getId()) {
            case R.id.sign_in_google:
                if (networkStatus) {
                    gSignIn.googleSignInMethod();
                } else {
                    hideProgressDialog();
                    showSnackbar(constraintLayout, getString(R.string.no_internet));
                }
                break;

            case R.id.sign_in_facebook:
                hideProgressDialog();
                /*
                if (networkStatus) {
                    LoginManager.getInstance().logInWithReadPermissions(login.this, permissionNeeds);
                } else {
                    hideProgressDialog();
                    showSnackbar(constraintLayout, getString(R.string.no_internet));
                }

                 */
                break;

            case R.id.skip:
                if (networkStatus) {
                    signInAnonymously();
                } else {
                    hideProgressDialog();
                    showSnackbar(constraintLayout, getString(R.string.no_internet));
                }
                break;

            case R.id.back_button:
                hideProgressDialog();
                finish();
                break;

            case R.id.login :
                String email_st = emailAddress.getText().toString();
                String password_st = password.getText().toString();
                if (networkStatus) {
                    if (!(email_st.isEmpty() || password_st.isEmpty())) {
                        signInWithEmailandPassword(email_st, password_st);
                    }
                    else{
                        hideProgressDialog();
                        Snackbar.make(constraintLayout, getString(R.string.allField), Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    hideProgressDialog();
                    showSnackbar(constraintLayout, getString(R.string.no_internet));
                }
                break;

            case R.id.create_account:
                hideProgressDialog();
                toActivity(login.this, signUp.class);
                break;

            case R.id.tv_forgot_password:
                hideProgressDialog();
                resetPassword(this, constraintLayout);
                break;
        }
    }



    private void goToMainMenu() {
        Intent intent = new Intent(login.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RC_GOOGLE_SIGN_IN) {
            if(resultCode!=0) {
                gSignIn.googleSignInActivityResult(data);
                showSnackbar(constraintLayout, getString(R.string.logged_in));
                hideProgressDialog();
            }
            else{
                showSnackbar(constraintLayout, getString(R.string.loginFailed));
                hideProgressDialog();
            }

        }
        /*
        else {
            if(resultCode!=0) {
                fSignIn.getCallbackManager().onActivityResult(requestCode, resultCode, data);
                showSnackbar(constraintLayout,  getString(R.string.logged_in));
                hideProgressDialog();
            }
            else{
                showSnackbar(constraintLayout, getString(R.string.loginFailed));
                hideProgressDialog();
            }

        }

         */


    }


    private void signInWithEmailandPassword(String email, String password) {

        FirebaseAuth.getInstance().
                signInWithEmailAndPassword(email, password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            showSnackbar(constraintLayout,getString(R.string.logged_in));
                            hideProgressDialog();
                            if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).isEmailVerified()) {
                                BaseActivity.toActivityClear(login.this, MainActivity.class);
                                finish();

                            } else {
                                showSnackbar(constraintLayout,getString(R.string.verifyEmailAddress));
                            }

                        } else {
                            try {
                                throw Objects.requireNonNull(task.getException());
                            } catch (FirebaseAuthInvalidUserException wrongEmail) {
                                showSnackbar(constraintLayout,getString(R.string.invalidEmail));
                            } catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                                showSnackbar(constraintLayout,getString(R.string.wrongPassword));
                            } catch (Exception e) {
                                showSnackbar(constraintLayout,"error " + e.getMessage());
                            }
                        }
                    }
                });

    }

    private void signInAnonymously(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            hideProgressDialog();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");

                            toActivityClear(login.this, MainActivity.class);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());

                            showSnackbar(constraintLayout,getString(R.string.authentificationFailed));
                        }
                    }
                });

    }


    private void printKeyHash() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.agung.aplikasi_skripsi",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {
        }
    }


    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        super.onInternetConnectivityChanged(isConnected);
        networkStatus = isConnected;
    }
}

