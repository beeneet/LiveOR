package com.tashambra.mobileapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by thitchener on 8/15/17.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBContract.FeedReaderContract.FeedEntry.TABLE_NAME + " (" +
                    DBContract.FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    DBContract.FeedReaderContract.FeedEntry.NAME + " TEXT," +
                    DBContract.FeedReaderContract.FeedEntry.PERCENT + " REAL," +
                    DBContract.FeedReaderContract.FeedEntry.VOLUME + " REAL," +
                    DBContract.FeedReaderContract.FeedEntry.INIT_TIME + " INT," +
                    DBContract.FeedReaderContract.FeedEntry.TIME_PASS + " REAL," +
                    DBContract.FeedReaderContract.FeedEntry.BAC + " REAL)";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBContract.FeedReaderContract.FeedEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
