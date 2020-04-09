package com.uprit.moneymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class DBAdapter {
    private static final String KEY_ROWID   = "_id";
    private static final String KEY_TITLE    = "title";
    private static final String KEY_PRICE    = "price";
    private static final String KEY_DATE    = "date";
    private static final String DB_TABLE    = "finance";
    private static final String[] ALL_COL = new String[] {
            KEY_ROWID,
            KEY_TITLE,
            KEY_PRICE,
            KEY_DATE
    };
    private final Context context;
    private DBHelper helper;
    private SQLiteDatabase db;

    DBAdapter(Context ctx){
        this.context = ctx;
        helper = new DBHelper(context);
    }

    // Open the database
    DBAdapter open(){
        try
        {
            db=helper.getWritableDatabase();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return this;
    }

    // Close the database
    void close(){
        try
        {
            helper.close();

        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    // Insert List
    long insertFinance(String title, String price, String date){
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_PRICE, price);
        initialValues.put(KEY_DATE, date);
        return db.insert(DB_TABLE, KEY_ROWID, initialValues);
    }

    // Retrieve List.
    ArrayList<Finance> getAllFinance(){
        Cursor c = db.query(DB_TABLE, ALL_COL, null, null, null, null, null);

        if(c == null) {
            Log.d(MainActivity.TAG, "UserDbAdapter.fetchAllUsers(): queryCursor = null ");
            return null;
        }

        ArrayList<Finance> list = new ArrayList<>();
        if(c.moveToFirst()){
            do {
                try {
                    String date = c.getString(3);

                    @SuppressLint("SimpleDateFormat")
                    Date aDate = new SimpleDateFormat("dd/MMM/yyyy").parse(date);

                    list.add(new Finance(c.getLong(0),c.getString(1),c.getString(2),aDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }while (c.moveToNext());
        }
        return list;
    }

    // Update List.
    boolean updateFinance(long RowId, String title, String price, String date){
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_PRICE, price);
        args.put(KEY_DATE, date);
        return db.update(DB_TABLE, args, KEY_ROWID + "=" + RowId, null) > 0;
    }


    // Delete List.
    boolean deleteFinance(long rowId){
        return db.delete(DB_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
