package in.ac.iiitd.dhcs.focus.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import in.ac.iiitd.dhcs.focus.Common.CommonUtils;

import in.ac.iiitd.dhcs.focus.Database.DbContract;
import in.ac.iiitd.dhcs.focus.Database.DbContract.ProductivityEntry;
import in.ac.iiitd.dhcs.focus.Database.FocusDbHelper;
import in.ac.iiitd.dhcs.focus.ListAdapters.TrackedAppListAdapter;
import in.ac.iiitd.dhcs.focus.MainTabActivity;
import in.ac.iiitd.dhcs.focus.R;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;

public class FocusService extends Service {

    private static String TAG = "FocusService";
    Context context;
    boolean isServiceRunning;
    PowerManager.WakeLock wl;

    FocusDbHelper dbs;
    @SuppressWarnings("rawtypes")
    private volatile Class b;
    private volatile Field c;
    private int d;
    private static String starttime, endtime, currentapp, currentpack;
    private static float duration;
    private Timer timer;
    private String activePackages = null;
    private ActivityManager a;
    private float Productivityscore;
    private boolean running = false;
    private int notifyID = 1555;
    static boolean counterflag = true ;
    private static String currentday="";
    NotificationManager mNotificationManager;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        context = getApplicationContext();
        isServiceRunning = false;
        currentapp = endtime = starttime = currentpack = null;
        duration = 0f;
        Productivityscore = 0.7f;
        dbs = new FocusDbHelper(context);
        getList();
        updateProductivity();
        a = (ActivityManager) context.getSystemService("activity");
        try {
            d = Class.forName(a.getClass().getName()).getField("PROCESS_STATE_TOP").getInt(a);
            return;
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        if (!isServiceRunning) {
            isServiceRunning = true;
        }

        timer = new Timer();
        TimerTask refresher = new TimerTask() {
            public void run() {


                try {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                        activePackages = a().getPackageName();
                    } else {
                        activePackages = getActivePackagesCompat();
                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }

                PackageManager pm = context.getPackageManager();
                long timeInMillis = System.currentTimeMillis();
                ApplicationInfo appinfo = null;
                try {
                    appinfo = pm.getApplicationInfo(activePackages, 0);
                } catch (NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                String appname = (String) (appinfo != null ? pm.getApplicationLabel(appinfo) : "Unknown");

                //Log.v(TAG, "TrackedAppListSize = " + TrackedAppListAdapter.trackedapps.size() + " " + TrackedAppListAdapter.trackedapps.containsKey(currentapp));
                        /* Base case to check for 1st time when app is null */
                if (currentapp == null) {
                    currentapp = appname;
                    currentpack = activePackages;
                    starttime = CommonUtils.unixTimestampToTime(timeInMillis);
                }
                        /* else if app is not null check for newapp with currentapp on stack & Only allow trackedapps */
                else if (!currentapp.equalsIgnoreCase(appname)) {
                    endtime = CommonUtils.unixTimestampToTime(timeInMillis);
                    duration = CommonUtils.getTimeDiff(starttime, endtime);

                    /* Only update db if it is under trackable apps & duration > 0*/
                    if(TrackedAppListAdapter.trackedapps.containsKey(currentapp) && duration > 0.0f) {
                        float Score = TrackedAppListAdapter.trackedapps.get(currentapp);
                        long Productivityduration = (long) (Score * duration / 10.0f);
                        if (getcount(currentapp, CommonUtils.unixTimestampToDate(timeInMillis)) > 0) {
                            updateData(currentapp, CommonUtils.unixTimestampToDate(timeInMillis), Productivityduration);
                            updateProductivity();
                        } else {
                            insertData(currentapp, currentpack, CommonUtils.unixTimestampToDate(timeInMillis),
                                    duration, Productivityduration);
                            updateProductivity();
                        }
                    }

                    starttime = endtime;
                    currentapp = appname;
                    currentpack = activePackages;

                }

                //Log.i(TAG,currentapp +appname+String.valueOf(currentapp.equalsIgnoreCase(appname)));

            }

            ;
        };

        timer.scheduleAtFixedRate(refresher, 0, 1000);
        wl = LockHandler.acquireWakeLock(context);
        wl.acquire();
        return START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();

        long timeInMillis = System.currentTimeMillis();
        // check before screen turning off
        if (currentapp != null && TrackedAppListAdapter.trackedapps.containsKey(currentapp)) {
            endtime = CommonUtils.unixTimestampToTime(timeInMillis);
            duration = CommonUtils.getTimeDiff(starttime, endtime);
            float Score = TrackedAppListAdapter.trackedapps.get(currentapp);
            long Productivityduration = (long) (Score * duration / 10.0f);

            if (getcount(currentapp, CommonUtils.unixTimestampToDate(timeInMillis)) > 0) {
                updateData(currentapp, CommonUtils.unixTimestampToDate(timeInMillis), Productivityduration);
            } else {
                insertData(currentapp, activePackages, CommonUtils.unixTimestampToDate(timeInMillis),
                        duration, Productivityduration);

            }
        }
        updateProductivity();
        timer.cancel();
        timer.purge();

        if (wl != null && wl.isHeld() == true) {
            try {
                wl.release();
                Log.d(TAG, "Wakelog SensorService realease");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    String getActivePackagesCompat() {

        Context context = getApplicationContext();
        ActivityManager am = (ActivityManager) context.
                getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        final ComponentName componentName = taskInfo.get(0).topActivity;
        final String activePackages;
        activePackages = componentName.getPackageName();
        //Toast.makeText(getApplicationContext(), activePackages,Toast.LENGTH_LONG).show();

        return activePackages;
    }


    public final ComponentName a() {
        Iterator iterator = a.getRunningAppProcesses().iterator();

        ComponentName componentname = null;
        if (iterator.hasNext()) {
            int j;
            ActivityManager.RunningAppProcessInfo runningappprocessinfo = (ActivityManager.RunningAppProcessInfo) iterator.next();
            String as[] = null;
            int i = 0;
            String s;
            try {
                if (a(runningappprocessinfo) == d) {

                    as = runningappprocessinfo.pkgList;
                    i = as.length;

                }
            } catch (Throwable throwable) {
            }
            j = 0;
            while (j < i) {

                s = as[j];
                if (TextUtils.equals(runningappprocessinfo.processName, "com.android.incallui")) {
                    return new ComponentName(s, "com.android.incallui");
                } else if (runningappprocessinfo.processName.startsWith(s)) {
                    componentname = new ComponentName(s, s);
                }
                j++;
            }

        }

        return componentname;
    }

    public int a(ActivityManager.RunningAppProcessInfo runningappprocessinfo) {
        int i;
        try {
            if (runningappprocessinfo.getClass().equals(b)) {
                return c.getInt(runningappprocessinfo);
            }
            b = Class.forName(runningappprocessinfo.getClass().getName());
            c = b.getField("processState");
            i = c.getInt(runningappprocessinfo);
        } catch (Throwable throwable) {
            return -1;
        }
        return i;
    }

    public void insertData(String AppName, String PackName, String Date, float time, long Productive) {
        SQLiteDatabase db = dbs.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ProductivityEntry.APP_NAME, AppName);
        values.put(ProductivityEntry.PACKAGE_NAME, PackName);
        values.put(ProductivityEntry.TRACKING_DATE, Date);
        values.put(ProductivityEntry.USAGE_DURATION, time);
        values.put(ProductivityEntry.PRODUCTIVE_DURATION, Productive);
        // Inserting 1Row
        db.insert(ProductivityEntry.TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    public int getcount(String AppName, String Date) {

        SQLiteDatabase db = dbs.getWritableDatabase();

        String sql = "select count(*) from '" + ProductivityEntry.TABLE_NAME + "' where " + ProductivityEntry.APP_NAME +
                " LIKE '" + AppName + "' and " + ProductivityEntry.TRACKING_DATE + " LIKE '" + Date + "'";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        int length = cursor.getInt(0);
        cursor.close();
        db.close();
        return length;
    }

    public void updateData(String AppName, String Date, float time) {

        SQLiteDatabase db = dbs.getWritableDatabase();
        String sql = "select " + ProductivityEntry.USAGE_DURATION + "," + ProductivityEntry._ID + " from '" + ProductivityEntry.TABLE_NAME + "'" +
                " where " + ProductivityEntry.APP_NAME + " LIKE '" + AppName + "' and " + ProductivityEntry.TRACKING_DATE + " LIKE '" + Date + "'";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        float oldTime = cursor.getFloat(0);
        float newTime = oldTime + time;
        Log.v(TAG, oldTime + " " + newTime);
        String rowid = cursor.getString(1);
        cursor.close();
        float Score = TrackedAppListAdapter.trackedapps.get(AppName);
        Score/=10.0f;
        ContentValues values = new ContentValues();
        values.put(ProductivityEntry.USAGE_DURATION, newTime);
        values.put(ProductivityEntry.PRODUCTIVE_DURATION, Score * newTime);
        db.update(ProductivityEntry.TABLE_NAME, values, ProductivityEntry._ID + "=" + rowid, null);
        db.close();
    }

    public void updateProductivity() {
        SQLiteDatabase db = dbs.getWritableDatabase();
        long timeInMillis = System.currentTimeMillis();
        String todaydate = CommonUtils.unixTimestampToDate(timeInMillis);
        CommonUtils.ProductivityScore = CommonUtils.TotalDuration = CommonUtils.TotalProductivity = 0L;

        String sql = "select " + ProductivityEntry.USAGE_DURATION + "," + ProductivityEntry.PRODUCTIVE_DURATION + " from '" + ProductivityEntry.TABLE_NAME + "'" +
                " where " + ProductivityEntry.TRACKING_DATE + " LIKE '" + todaydate + "'";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
        }
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            CommonUtils.TotalDuration += cursor.getLong(0);
            CommonUtils.TotalProductivity += cursor.getLong(1);
        }
        cursor.close();
        CommonUtils.ProductivityScore = (long) (((float) CommonUtils.TotalProductivity / (float) CommonUtils.TotalDuration) * 100);
        Log.v(TAG, CommonUtils.TotalProductivity + " " + CommonUtils.TotalDuration + " " + 100 * ((float) CommonUtils.TotalProductivity / (float) CommonUtils.TotalDuration));

        if(!currentday.equalsIgnoreCase(CommonUtils.unixTimestampToDate(System.currentTimeMillis()))){
        counterflag = true;
        currentday = CommonUtils.unixTimestampToDate(System.currentTimeMillis());
        }

        Log.v(TAG,String.valueOf(CommonUtils.gettimeinhours(System.currentTimeMillis())));
        //Fire notification once Score > Goal
        if(CommonUtils.ProductivityScore>=CommonUtils.ProductivityGoal &&
                CommonUtils.gettimeinhours(System.currentTimeMillis())==23 &&
                counterflag==true){
            String msg = "Good Job! Your day has been productive";
            PushNotification(msg);
            counterflag = false;
        }
        //Firing a negative notification
        else if(CommonUtils.ProductivityScore<CommonUtils.ProductivityGoal &&
                CommonUtils.gettimeinhours(System.currentTimeMillis())==23 &&
                counterflag==true){
            String msg = "You were distracted! Better luck tomorrow";
            PushNotification(msg);
            counterflag = false;
        }

        db.close();
    }


    public void getList(){
        SQLiteDatabase db = dbs.getWritableDatabase();
        String sql = "select * from '" + DbContract.TrackedAppEntry.TABLE_NAME + "'";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
        }

        TrackedAppListAdapter.trackedapps.clear();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String appName = cursor.getString(cursor.getColumnIndex(DbContract.TrackedAppEntry.APP_NAME));
            Float Score = cursor.getFloat(cursor.getColumnIndex(DbContract.TrackedAppEntry.PRODUCTIVITY_SCORE));
            TrackedAppListAdapter.trackedapps.put(appName,Score);
        }
        cursor.close();
        db.close(); // Closing database connection
    }

    public void PushNotification(String msg){

        int pid = android.os.Process.myPid();
        Intent notiintent = new Intent(this, MainTabActivity.class);

        notiintent.setAction(Long.toString(System.currentTimeMillis()));
        int requestID = (int) System.currentTimeMillis();
        PendingIntent pIntent = PendingIntent.getActivity(this, requestID, notiintent,PendingIntent.FLAG_UPDATE_CURRENT);

        // Sets an ID for the notification, so it can be updated
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Builder mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Focus")
                .setContentIntent(pIntent)
                .setOngoing(false);

        mNotifyBuilder.setSmallIcon(R.mipmap.ic_notification);

        mNotifyBuilder.setContentText(msg);

        Notification temp = mNotifyBuilder.build();

        temp.defaults |= Notification.DEFAULT_VIBRATE;
        mNotificationManager.notify(notifyID,temp);
    }
}