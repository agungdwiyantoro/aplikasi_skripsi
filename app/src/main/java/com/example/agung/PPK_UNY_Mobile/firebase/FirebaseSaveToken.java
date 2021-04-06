package com.example.agung.PPK_UNY_Mobile.firebase;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

public class FirebaseSaveToken {
    private static final String TAG = "FirebaseSaveToken";

    public static void saveToken(String email){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference user_ref = firebaseFirestore.
                collection("users");

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task2 -> {
            if(task2.isSuccessful()){
                //model_user mu = new model_user(email, task2.getResult().getToken());
                //user_ref.document(email).set(mu);
            }
        });
    }
}
