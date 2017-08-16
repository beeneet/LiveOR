package com.tashambra.mobileapp;

import android.provider.BaseColumns;

/**
 * Created by thitchener on 8/15/17.
 */

public class DBContract {

    public final class FeedReaderContract {
        // To prevent someone from accidentally instantiating the contract class,
        // make the constructor private.
        private FeedReaderContract() {}

        /* Inner class that defines the table contents */
        public class FeedEntry implements BaseColumns {
            public static final String TABLE_NAME = "drinks";
            public static final String NAME = "name";
            public static final String PERCENT = "percent";
            public static final String VOLUME = "volume";
            public static final String INIT_TIME = "init_time";
            public static final String TIME_PASS = "time_pass";
            public static final String BAC = "bac";

        }

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                        FeedEntry._ID + " INTEGER PRIMARY KEY," +
                        FeedEntry.NAME + " TEXT," +
                        FeedEntry.PERCENT + " REAL," +
                        FeedEntry.VOLUME + " REAL," +
                        FeedEntry.INIT_TIME + " INT," +
                        FeedEntry.TIME_PASS + " REAL," +
                        FeedEntry.BAC + " REAL)";


        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
    }

}
