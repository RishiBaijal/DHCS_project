package in.ac.iiitd.dhcs.focus.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import in.ac.iiitd.dhcs.focus.Objects.ProductivityObject;

public class CommonUtils {
    private static final String TAG = "CommonUtils";
    public static long ProductivityScore=0L;
    public static long TotalDuration=0L;
    public static long TotalProductivity=0L;

    /**
     * Convert unixtime in milliseconds to human readable data time of format dd/MM/yyyy HH:mm:ss
     * @param t unixtime
     * @return corresponding human readable datetime
     * */
    public static String unixTimestampToDate(long t){
        String str = null;
        str = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date(t));
        return str;
    }
    
    public static String unixTimestampToTime(long t){
        String str = null;
        str = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date(t));
        return str;
    }
    
    public static long getTimeDiff(String start,String end){
    	java.text.DateFormat df = new java.text.SimpleDateFormat("HH:mm:ss");
        java.util.Date date1 = null,date2 = null;
		try {
			date1 = df.parse(start);
			date2 = df.parse(end);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return ((date2.getTime() - date1.getTime()));
    }
}
