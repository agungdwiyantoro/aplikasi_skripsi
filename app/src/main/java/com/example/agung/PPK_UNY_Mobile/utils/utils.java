package com.example.agung.PPK_UNY_Mobile.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class utils {

    public static String date_final(Date start, Date end){
        String startDate = timeMilisToDate_with_hours(start);
        String endDate = timeMilisToDate_with_hours(end);
        return  startDate + " - " + endDate;
    }


    public static String timeMilisToDate(Date stDate) {
        DateFormat simple = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        Date result = new Date(stDate.getTime());
        return simple.format(result);
    }

    public static String timeMilisToDate_with_hours(Date stDate) {
        DateFormat simple = new SimpleDateFormat("dd MMM yyyy hh:mm aa", Locale.getDefault());
        Date result = new Date(stDate.getTime());
        return simple.format(result);
    }

    public static Date timeMilisToDate_appliedJobs(String stDate) {
        DateFormat simple = new SimpleDateFormat("dd MMM yyyy hh:mm aa", Locale.getDefault());
        try {
            return simple.parse(stDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int filesize_in_kiloBytes(int fileLength) {
        return Math.round(fileLength/1024);
    }

}
