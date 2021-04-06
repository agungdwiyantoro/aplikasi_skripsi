package com.example.agung.PPK_UNY_Mobile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.Objects;

public class signUp extends BaseActivity implements View.OnClickListener{

    private EditText emailAddress, password;
    private LinearLayout linearLayout;

    private String email_st;
    private String password_st;
    private boolean networkStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        init();
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_create:
                if (networkStatus) {
                    email_st = emailAddress.getText().toString();
                    password_st = password.getText().toString();

                    if(email_st.isEmpty()||password_st.isEmpty()) {
                        showSnackbar(linearLayout, getString(R.string.allField));
                    }else{
                        signInWithEmailAndPassword();
                    }
                }
                else{
                    showSnackbar(linearLayout, getString(R.string.no_internet));
                }
                break;
            case R.id.tv_already_a_user:
                toActivityClear(signUp.this, login.class);
                finish();
                break;
            case R.id.back_button :
                finish();
                break;
        }

    }

    void init(){
        Button bt_create_account = findViewById(R.id.bt_create);
        TextView login = findViewById(R.id.tv_already_a_user);
        linearLayout = findViewById(R.id.linear_layout);
        ImageView backButton = findViewById(R.id.back_button);
        emailAddress = findViewById(R.id.email_address);
        password = findViewById(R.id.password);
        TextView tvToolbar = findViewById(R.id.tv_toolbar);
        tvToolbar.setText(getString(R.string.sign_up));

        bt_create_account.setOnClickListener(this);
        login.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }


    void signInWithEmailAndPassword(){
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
       // final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        firebaseAuth.
                createUserWithEmailAndPassword(email_st, password_st).
                addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    showSnackbar(linearLayout,getString(R.string.successRegister));
                                    emailAddress.setText("");
                                    password.setText("");
                                }
                                else{
                                    showSnackbar(linearLayout,getString(R.string.errorOccured));
                                }
                            }
                        });
                    }
                    else{
                        try{
                            throw Objects.requireNonNull(task.getException());
                        }catch (FirebaseAuthWeakPasswordException weakPassword){
                            showSnackbar(linearLayout, getString(R.string.passwordTooWeak));
                        }catch (FirebaseAuthInvalidCredentialsException malformedEmail){
                            showSnackbar(linearLayout,getString(R.string.invalidEmail));
                        }catch (FirebaseAuthUserCollisionException emailAlreadyExist){
                            showSnackbar(linearLayout,getString(R.string.emailUsed));
                        }catch (Exception e) {

                        }
                    }

                });
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        super.onInternetConnectivityChanged(isConnected);
        networkStatus = isConnected;
    }
}
