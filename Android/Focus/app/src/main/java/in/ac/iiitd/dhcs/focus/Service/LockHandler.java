package in.ac.iiitd.dhcs.focus.Service;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.util.Log;

public class LockHandler {
    /** Wake lock to be acquired before running the manager service*/
	// create a class member variable.
	private static PowerManager.WakeLock mCpuWakeLock = null;

    /** Acquires the wake lock */
    public static PowerManager.WakeLock acquireWakeLock(Context context) {

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        mCpuWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Test");

        return mCpuWakeLock;
    }

    /** Release the wake lock */
    public static void releaseWakeLock() {
        if (mCpuWakeLock != null) {
            mCpuWakeLock.release();
            mCpuWakeLock = null;
        }
    }
}
