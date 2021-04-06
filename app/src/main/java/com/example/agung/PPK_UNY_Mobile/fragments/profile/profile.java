package com.example.agung.PPK_UNY_Mobile.fragments.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.agung.PPK_UNY_Mobile.BaseActivity;
import com.example.agung.PPK_UNY_Mobile.unused.IKARegistration;
import com.example.agung.PPK_UNY_Mobile.MainActivity;
import com.example.agung.PPK_UNY_Mobile.R;
import com.example.agung.PPK_UNY_Mobile.applied_jobs;
import com.example.agung.PPK_UNY_Mobile.login;
import com.example.agung.PPK_UNY_Mobile.fragments.profile.cv_resume.resumeProfile;
import com.example.agung.PPK_UNY_Mobile.signIn.facebookSignIn;
import com.example.agung.PPK_UNY_Mobile.signIn.googleSignIn;
import com.example.agung.PPK_UNY_Mobile.signUp;
import com.example.agung.PPK_UNY_Mobile.uploadDiploma;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import static com.example.agung.PPK_UNY_Mobile.BaseActivity.getEmail;
import static com.example.agung.PPK_UNY_Mobile.BaseActivity.toActivity;
import static com.example.agung.PPK_UNY_Mobile.alertDialogs.alertDialogs.DialogFormGuess;
import static com.example.agung.PPK_UNY_Mobile.alertDialogs.alertDialogs.alert_tracer_study;


public class profile extends Fragment implements View.OnClickListener{

    private final String TAG = "profile";


    private ImageView profilePictureAccount;
    private TextView emailAccount;
    private Button signOut;
    private Button login;
    private Button signUp;
    private Button appliedJobs;
    private Button about;
    private View border;
    private googleSignIn gSignIn;
    private facebookSignIn fSignIn;
    private LinearLayout linearLayout;
    private String pathDiploma, pathResume;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);

        init(view);
        method_updateUI();
        return view;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.notifications:
                toActivity(getActivity(), settings.class);
                break;

            case R.id.upload_resume:
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null && user.getProviderData().size()!=1) {
                    toActivity(getActivity(), resumeProfile.class);

                /*
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null && user.getProviderData().size()!=1) {
                    MainActivity.metaData(meta_data -> {
                        if(meta_data!=null){
                            toActivity(getActivity(), resumeProfile.class);
                        }
                        else{
                            toActivity(getActivity(), uploadResume.class);
                        }

                    }, getActivity(), linearLayout, pathResume);

                 */

                } else {
                    DialogFormGuess(getActivity());
                }

                break;

            case R.id.sign_up:
                toActivity(getActivity(), signUp.class);
               // getActivity().finish();
                break;

            case R.id.tv_login:
                toActivity(getActivity(), login.class);
              //  getActivity().finish();
                break;

            case R.id.sign_out:
                ((MainActivity)getActivity()).showProgressDialog();
                signOut();
                break;


            case R.id.applied_jobs:
                toActivity(getActivity(), applied_jobs.class);
                break;

            case R.id.tracer_study :
                //toActivity(getActivity(), tracerStudy.class);
                //new tracerStudy("fuck");
                //checkAnggotaIKAregistration();
                toActivity(getActivity(), tracerStudy.class);
                break;

            case R.id.registrasi_anggota_IKA :
                BaseActivity.metaDataDiploma(mIKAmember -> {
                    if(mIKAmember != null) {
                        toActivity(getActivity(), IKARegistration.class);
                    }
                    else{

                        toActivity(getActivity(), uploadDiploma.class);
                    }
                }, getActivity(), linearLayout, pathDiploma);


                break;
            case R.id.about :
                toActivity(getActivity(), com.example.agung.PPK_UNY_Mobile.fragments.profile.about.class);
                break;
        }


    }

    private void init(View view) {
        TextView notification = view.findViewById(R.id.notifications);
        TextView upload_resume = view.findViewById(R.id.upload_resume);
        about = view.findViewById(R.id.about);
        signUp = view.findViewById(R.id.sign_up);
        login = view.findViewById(R.id.tv_login);
        signOut = view.findViewById(R.id.sign_out);
        emailAccount = view.findViewById(R.id.tv_email);
        profilePictureAccount = view.findViewById(R.id.iv_pp);
        //appliedJobs = view.findViewById(R.id.applied_jobs);
        Button tracerStudy = view.findViewById(R.id.tracer_study);
        Button IKARegistration = view.findViewById(R.id.registrasi_anggota_IKA);
        border = view.findViewById(R.id.border_line);
        ImageView backButton = view.findViewById(R.id.back_button);
        backButton.setVisibility(View.GONE);
        TextView tvToolbar = view.findViewById(R.id.tv_toolbar);
        tvToolbar.setText(getString(R.string.profile));

        emailAccount.setSelected(true);
        notification.setOnClickListener(this);
        upload_resume.setOnClickListener(this);
        signUp.setOnClickListener(this);
        signOut.setOnClickListener(this);
        login.setOnClickListener(this);
        about.setOnClickListener(this);
//        appliedJobs.setOnClickListener(this);
        IKARegistration.setOnClickListener(this);
        tracerStudy.setOnClickListener(this);


        gSignIn = new googleSignIn(getActivity());
        fSignIn = new facebookSignIn(getActivity());
        linearLayout = view.findViewById(R.id.linear_layout);
    }


    private void setAccountInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Picasso.get()
                    .load(user.getPhotoUrl())
                    .into(profilePictureAccount);

            if (user.getProviderData().get(1).getProviderId().equals("facebook.com")) {
                emailAccount.setText(user.getProviderData().get(1).getEmail());
            } else {
                emailAccount.setText(user.getEmail());

            }
    }

    private void method_updateUI() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
/*
        for (UserInfo userIn: FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
           // if (userIn.getProviderId().equals("facebook.com")) {
                System.out.println("User is signed in with " + userIn.getProviderId());
          //  }
        }

 */
        if (user !=null && user.getProviderData().size()!=1) {
            login.setVisibility(View.GONE);
            signUp.setVisibility(View.GONE);
            emailAccount.setVisibility(View.VISIBLE);
            profilePictureAccount.setVisibility(View.VISIBLE);
            signOut.setVisibility(View.VISIBLE);
            //appliedJobs.setVisibility(View.VISIBLE);
            border.setVisibility(View.VISIBLE);

            pathDiploma = "diploma/" + getEmail() + "/" + "diploma(" + getEmail() + ")";
            pathResume = "resumes/" + getEmail() + "/" + "resume(" + getEmail() + ")";
            setAccountInformation();

        } else {
            emailAccount.setVisibility(View.GONE);
            profilePictureAccount.setVisibility(View.GONE);
            signOut.setVisibility(View.GONE);
            border.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
            signUp.setVisibility(View.VISIBLE);
            //appliedJobs.setVisibility(View.INVISIBLE);
        }
    }


    private void signOut(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user.getProviderData().get(1).getProviderId().equals("facebook.com")){
            fSignIn.facebookSignOut( () -> ((MainActivity)getActivity()).hideProgressDialog(), () -> ((MainActivity)getActivity()).showSnackbar(linearLayout, getString(R.string.logged_out)), this::method_updateUI);
        }
        else if(user.getProviderData().get(1).getProviderId().equals("google.com")){
            gSignIn.configureGoogleSignIn();
            gSignIn.googleSignOut(this::method_updateUI, () -> ((MainActivity)getActivity()).hideProgressDialog(), () -> ((MainActivity)getActivity()).showSnackbar(linearLayout,getString(R.string.logged_out)));
        }
        else{
            FirebaseAuth.getInstance().signOut();
            method_updateUI();
            ((MainActivity)getActivity()).showSnackbar(linearLayout,getString(R.string.logged_out));
            ((MainActivity)getActivity()).hideProgressDialog();
        }
    }

    private void checkAnggotaIKAregistration(){
        String path = "diploma/" + getEmail() + "/" + "diploma(" + getEmail() + ")";
        BaseActivity.metaDataDiploma(mIKAmember -> {
            if(mIKAmember != null) {
                checkAnggotaIKAmember(mIKAmember.getNIM());
            }
            else{
                alert_tracer_study(getActivity());
            }
        }, getActivity(), linearLayout, path);
    }

    private void checkAnggotaIKAmember(String studentID){
        FirebaseFirestore.getInstance().collection("IKA_member")
                .document(studentID)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        if(task.getResult().getString("nim")!=null) {
                            toActivity(getActivity(), tracerStudy.class);
                            Log.d(TAG, "registered user");
                        }
                        else{
                            BaseActivity.showSnackbarstatic(linearLayout, getString(R.string.wait_for_verification));
                        }
                        //setPhotoDiploma();
                    }
                    else{
                        alert_tracer_study(getActivity());
                    }

                });
    }

}
