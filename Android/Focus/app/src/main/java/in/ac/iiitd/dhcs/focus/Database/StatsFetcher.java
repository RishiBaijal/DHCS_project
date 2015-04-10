package in.ac.iiitd.dhcs.focus.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import in.ac.iiitd.dhcs.focus.StatsObjects.ProductivePercentStatObject;
import in.ac.iiitd.dhcs.focus.StatsObjects.ProductiveTimeStatObject;
import in.ac.iiitd.dhcs.focus.StatsObjects.WeeklyPercentStatObject;
import in.ac.iiitd.dhcs.focus.StatsObjects.WeeklyTimeStatObject;

/**
 * Created by vedantdasswain on 10/04/15.
 */
public class StatsFetcher {
    static  FocusDbHelper mDbHelper;
    private static final String TAG="StatsFetcher";

    public static ArrayList<ProductiveTimeStatObject> getProductiveTime(Context context,String appName) {
        ArrayList<ProductiveTimeStatObject> statList=new ArrayList<ProductiveTimeStatObject>();
        mDbHelper = new FocusDbHelper(context);

        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(System.currentTimeMillis()).toString();

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String query = "SELECT " + DbContract.ProductivityEntry.TRACKING_DATE + ","
                + " SUM" + "(" + DbContract.ProductivityEntry.PRODUCTIVE_DURATION + "),"
                + " SUM" + "(" + DbContract.ProductivityEntry.USAGE_DURATION + ")"
                + " FROM " + DbContract.ProductivityEntry.TABLE_NAME
//                + " WHERE " + DbContract.ProductivityEntry.TRACKING_DATE + "!=" + today
                + " GROUP BY " + DbContract.ProductivityEntry.TRACKING_DATE
                + " ORDER BY " + DbContract.ProductivityEntry.TRACKING_DATE + " ASC";

        Cursor cursor = db.rawQuery(
                query, null);

        if (cursor != null && cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {

                String date = cursor.getString(0);
                long p_dur = cursor.getLong(1);
                long u_dur = cursor.getLong(2);
                Log.d(TAG, "Time entry: " + date + ", " + p_dur + ", " + u_dur);

                long dateLong= 0;
                try {
                    dateLong = sdf.parse(date).getTime();
                    ProductiveTimeStatObject ptso=new ProductiveTimeStatObject(dateLong,p_dur,u_dur);
                    statList.add(ptso);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                cursor.moveToNext();
            }
        }

        return  statList;
    }

    public static ArrayList<ProductivePercentStatObject> getProductivePercent(Context context,String appName) {
        ArrayList<ProductiveTimeStatObject> timeList=getProductiveTime(context,appName);
        ArrayList<ProductivePercentStatObject> statList=new ArrayList<ProductivePercentStatObject>();

        for(ProductiveTimeStatObject ptso:timeList) {
            float percent=((float)ptso.getProdDur()/(float)ptso.getUseDur())*100;
            ProductivePercentStatObject ppso = new ProductivePercentStatObject(ptso.getDate(),percent);
            statList.add(ppso);
            Log.d(TAG, "Percent entry: " + ppso.getDate() + ", " + percent);
        }

        return  statList;
    }

    public static WeeklyTimeStatObject[] getWeeklyTime(Context context,String appName) {
        ArrayList<ProductiveTimeStatObject> timeList=getProductiveTime(context,appName);
        WeeklyTimeStatObject[] statList=new WeeklyTimeStatObject[8];

        String[] daysOfWeek={"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
        long[] prod_dur=new long[8];
        long[] use_dur=new long[8];
        int[] count=new int[8];

        //Initialize
        for(int i=0;i<8;i++){
            prod_dur[i]=0;
            use_dur[i]=0;
            count[i]=0;
        }

        for(ProductiveTimeStatObject ptso:timeList) {
            Calendar cal=Calendar.getInstance();
            cal.setTimeInMillis(ptso.getDate());
            int day=cal.DAY_OF_WEEK;

            prod_dur[day]+=ptso.getProdDur();
            use_dur[day]+=ptso.getUseDur();
            count[day]++;
        }

        for(int i=1;i<8;i++){
            long prod=prod_dur[i]/count[i];
            long use=use_dur[i]/count[i];
            WeeklyTimeStatObject wtso=new WeeklyTimeStatObject(daysOfWeek[i-1],prod,use);
            statList[i]=wtso;
        }

        return  statList;
    }

    public static WeeklyPercentStatObject[] getWeeklyPercent(Context context,String appName) {
        ArrayList<ProductivePercentStatObject> timeList=getProductivePercent(context,appName);
        WeeklyPercentStatObject[] statList=new WeeklyPercentStatObject[8];

        String[] daysOfWeek={"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
        long[] prod_per=new long[8];
        int[] count=new int[8];

        //Initialize
        for(int i=0;i<8;i++){
            prod_per[i]=0;
            count[i]=0;
        }

        for(ProductivePercentStatObject ppso:timeList) {
            Calendar cal=Calendar.getInstance();
            cal.setTimeInMillis(ppso.getDate());
            int day=cal.DAY_OF_WEEK;

            prod_per[day]+=ppso.getProdPercent();
            count[day]++;
        }

        for(int i=1;i<8;i++){
            float prod=(float)prod_per[i]/(float)count[i];
            WeeklyPercentStatObject wpso=new WeeklyPercentStatObject(daysOfWeek[i-1],prod);
            statList[i]=wpso;
        }

        return  statList;
    }
}
