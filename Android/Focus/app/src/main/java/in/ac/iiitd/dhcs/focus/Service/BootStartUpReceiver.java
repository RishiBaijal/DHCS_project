package in.ac.iiitd.dhcs.focus.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;

import in.ac.iiitd.dhcs.focus.MainTabActivity;

public class BootStartUpReceiver extends BroadcastReceiver{

    private static ScreenStateReceiver mScreenStateReceiver;
	private static String TAG = BootStartUpReceiver.class.getName();
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		// Start Service On Boot Start Up
		Log.i(TAG, "Boot up : received");
        Intent i = new Intent(context, MainTabActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

}
