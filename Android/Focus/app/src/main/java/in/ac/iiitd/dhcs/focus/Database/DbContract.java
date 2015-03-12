package in.ac.iiitd.dhcs.focus.Database;

import android.provider.BaseColumns;

/**
 * Created by vedantdasswain on 11/03/15.
 */
public class DbContract {
        public DbContract() {}

        private static final String TEXT_TYPE = " TEXT";
        private static final String REAL_TYPE = " REAL";
        private static final String COMMA_SEP = ",";

        /* Any app that the user chooses to track will be placed in these column
        with its productivity score set by the user. 
        We will only track and process the time spent on these apps*/
        public static abstract class TrackedAppEntry implements BaseColumns {
            public static final String TABLE_NAME = "Tracked_Apps";
            public static final String APP_NAME = "App_Name";
            public static final String PRODUCTIVITY_SCORE = "Productivity_Score"; 

            public static final String SQL_CREATE_ENTRIES =
                    "CREATE TABLE " + DbContract.TrackedAppEntry.TABLE_NAME + " (" +
                            DbContract.TrackedAppEntry._ID + " INTEGER PRIMARY KEY," +
                            TrackedAppEntry.APP_NAME + TEXT_TYPE + COMMA_SEP +
                            TrackedAppEntry.PRODUCTIVITY_SCORE + REAL_TYPE +
                            " )";
            public static final String SQL_DELETE_ENTRIES =
                    "DROP TABLE IF EXISTS " + DbContract.TrackedAppEntry.TABLE_NAME;

        }

        /*Every time the user uses a tracked app and then stops/pauses it, 
        this table must be updated. These are the columns that we will use to analyse the
        productivity*/
        public static abstract class ProductivityEntry implements BaseColumns {
            public static final String TABLE_NAME = "Productivity";
            public static final String APP_NAME = "App_Name";
            public static final String TRACKING_DATE = "Tracking_Date"; //Store dates in yyyy-MM-dd format
            public static final String USAGE_DURATION = "Usage_Duration";   //Store time spent in millis
            public static final String PRODUCTIVE_DURATION = "Productive_Duration"; //Store productive time spent

            public static final String SQL_CREATE_ENTRIES =
                    "CREATE TABLE " + DbContract.ProductivityEntry.TABLE_NAME + " (" +
                            DbContract.ProductivityEntry._ID + " INTEGER PRIMARY KEY," +
                            ProductivityEntry.APP_NAME+TEXT_TYPE+COMMA_SEP+
                            ProductivityEntry.TRACKING_DATE + TEXT_TYPE + COMMA_SEP +
                            ProductivityEntry.USAGE_DURATION + REAL_TYPE + COMMA_SEP +
                            ProductivityEntry.PRODUCTIVE_DURATION + REAL_TYPE +
                            " )";
            public static final String SQL_DELETE_ENTRIES =
                    "DROP TABLE IF EXISTS " + DbContract.ProductivityEntry.TABLE_NAME;
        }
    
}
