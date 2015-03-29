package in.ac.iiitd.dhcs.focus.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class ScreenStateReceiver extends BroadcastReceiver
{

 private static String TAG = "ScreenStateReceiver";

 public ScreenStateReceiver()
 {

 }

 public void onReceive(Context context, Intent intent)
 {
     try
     {
         if (intent.getAction().equals("android.intent.action.SCREEN_OFF") || intent.getAction().equals("android.intent.action.DREAMING_STARTED"))
         {
             stopFocusService(context.getApplicationContext());
        	 return;
         }
     }
     catch (Throwable throwable)
     {
         ayy.a(throwable);
         return;
     }
     if (intent.getAction().equals("android.intent.action.SCREEN_ON") || intent.getAction().equals("android.intent.action.DREAMING_STOPPED"))
     {
         startFocusService(context.getApplicationContext());
         return;
     }
     
     return;
 }

 
 
 public static void startFocusService(Context context){		
		Intent intent = new Intent(context, FocusService.class);
	    Log.d(TAG, "Calling start service");
		context.startService(intent);  
	}

 public static void stopFocusService(Context context){		
		Intent intent = new Intent(context, FocusService.class);
	    Log.d(TAG, "Calling stop service");
		context.stopService(intent);  
	}

 
 
}
