package com.example.agung.PPK_UNY_Mobile.SQLiteDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.agung.PPK_UNY_Mobile.model.model_appliedJobs;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "D_APPLIED_JOBS";

    private final String TABLE_NAME = "APPLIED_JOBS";

    private final String ID = "ID";
    private final String JOB_NAME = "JOB_NAME";
    private final String COMPANY_NAME = "COMPANY_NAME";
    private final String DATE = "DATE";
    private final String JOB_ID = "JOB_ID";
    private final String EMAIL_ADDRESS = "EMAIL_ADDRESS";
    private final String DETAIL = "DETAIL";



    private final String TAG = "SQLiteHelper";



    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");
        db.execSQL(CREATE_TABLE());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       // Log.d(TAG, "onUpgrade" + DATABASE_VERSION);
     //   db.execSQL("CREATE TABLE IF NOT EXISTS  " + TABLE_NAME );
       // DATABASE_VERSION++;
      //  DATABASE_VERSION++;
      //  Log.d(TAG, "db " + DATABASE_VERSION);
        //db.execSQL(CREATE_TABLE(TABLE_NAME));
        onCreate(db);
    }


    private String CREATE_TABLE(){
        return "CREATE TABLE IF NOT EXISTS  " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                JOB_NAME + " VARCHAR(50)," +
                COMPANY_NAME + " VARCHAR(20)," +
                DATE + " VARCHAR(20)," +
                JOB_ID + " VARCHAR(50)," +
                EMAIL_ADDRESS + " VARCHAR(50)," +
                DETAIL + " VARCHAR(50)" +
                ");";
    }



    public void insertData(model_appliedJobs apJobs, String email_address){
        SQLiteDatabase db  = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(JOB_NAME, apJobs.getJobName());
        values.put(COMPANY_NAME, apJobs.getCompanyName());
        values.put(DATE, apJobs.getDate());
        values.put(JOB_ID, apJobs.getJobID());
        values.put(EMAIL_ADDRESS, email_address);
        values.put(DETAIL, apJobs.getDetail());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<model_appliedJobs> getAppliedJobs(String email) {
        Log.d(TAG, "getAllAppliedJobs");
        List<model_appliedJobs> listApJobs = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + EMAIL_ADDRESS + " = " + "'" + email + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                model_appliedJobs apJobs = new model_appliedJobs();
                apJobs.setCompanyName(cursor.getString(cursor.getColumnIndex(COMPANY_NAME)));
                apJobs.setJobName(cursor.getString(cursor.getColumnIndex(JOB_NAME)));
                apJobs.setDate((cursor.getString(cursor.getColumnIndex(DATE))));
                apJobs.setDetail((cursor.getString(cursor.getColumnIndex(DETAIL))));
                listApJobs.add(apJobs);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listApJobs;
    }

    public int getJobID(String keyJobID, String email) {
        Log.d(TAG, "getJobID");
        SQLiteDatabase db = getReadableDatabase();
        int temp ;

        Cursor cursor = db.query(TABLE_NAME, new String[]{ID,
                        JOB_NAME, COMPANY_NAME, DATE, JOB_ID}, JOB_ID + "=?" + " AND " + EMAIL_ADDRESS + "=?",
                new String[]{String.valueOf(keyJobID), String.valueOf(email)}, null, null, null, null);
        temp = cursor.getCount();
        cursor.close();
        return temp;
    }

    public int getID(String email) {
        Log.d(TAG, "getJobID");
        SQLiteDatabase db = getReadableDatabase();
        int temp ;

        Cursor cursor = db.query(TABLE_NAME, new String[]{ID,
                        JOB_NAME, COMPANY_NAME, DATE, JOB_ID}, ID + "=?",
                new String[]{String.valueOf(email)}, null, null, null, null);
        temp = cursor.getCount();
        cursor.close();
        return temp;
    }

    public int getJobAppliedCount(String email) {
        String countQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + EMAIL_ADDRESS + " = " + "'" + email + "'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int temp = cursor.getCount();
        cursor.close();

        // return count
        return temp;
    }

/*
    public void updateMyjobs(int id, String truefalse)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SAVE, truefalse);
        db.update(TABLE_NAME_MY_JOB, values , JOB_ID + "=?",  new String[] { String.valueOf(id)} );
        db.close();
    }

    public void deleteKey(String ID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_MY_JOB, JOB_ID + "=?", new String[] { String.valueOf(ID)} );
        db.close();
    }



    public int getNotesCount(String table_name) {
        String countQuery = "SELECT  * FROM " + table_name;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

*/
}


