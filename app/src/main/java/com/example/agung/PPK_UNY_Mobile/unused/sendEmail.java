package com.example.agung.PPK_UNY_Mobile.unused;

import android.os.AsyncTask;

import java.io.IOException;

import javax.mail.MessagingException;

public class sendEmail extends AsyncTask<Void, Void, Void>{
    @Override
    protected Void doInBackground(Void... voids) {

        try {
            GmailService gmailService = new GmailServiceImpl(new com.google.api.client.http.javanet.NetHttpTransport());
            gmailService.setGmailCredentials(new GmailCredentials(
                    "agung.dwiyantoro@gmail.com",
                    "457901691142-guddj1ssllgj42tu27q9lre591i6sif0.apps.googleusercontent.com",
                    "piREu2iY2kigfIuQR8uNDPBf",
                    "ya29.Il-UB2i3jyQt9HOd4YrMIa7TBpge1bMsuFwGGGzYXKles6N71dn77qz0452sww-MOqqVuF1nozXpsc8cGm935Bk_z-19QPlLs5u-j4Og7EC654ROu9wWaurPsYYhl9RS5Q",
                    "1/LPFH3sR8SlBVvm5X7DY-J3Ita3WyAEHGFo0ziNtqxVk"));


            gmailService.sendMessage("jasonfreddy13th@gmail.com", "Asoe", "anjing");
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
