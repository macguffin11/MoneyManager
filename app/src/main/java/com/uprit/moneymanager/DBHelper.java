package com.uprit.moneymanager;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG         = "DB Adapter";
    private static final String DB_NAME     = "MyDB";
    private static final int    DB_VERSION  = 1;
    private static final String DB_CREATE   =
            "create table finance ("
            + "_id integer primary key autoincrement, "
            + "title text not null, "
            + "price text not null, "
            + "date text not null);";
    DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(DB_CREATE);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to"
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS finance");
        onCreate(db);
    }
}
