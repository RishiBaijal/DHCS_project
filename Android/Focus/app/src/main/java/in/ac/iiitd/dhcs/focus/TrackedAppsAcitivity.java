package in.ac.iiitd.dhcs.focus;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import in.ac.iiitd.dhcs.focus.ListAdapters.TrackedAppListAdapter;
import in.ac.iiitd.dhcs.focus.Objects.UserAppObject;


public class TrackedAppsAcitivity extends ActionBarActivity {

    private static String TAG = "TrackedAppsActivity";

    private static TrackedAppListAdapter trackedAppsListAdapter;
    private static ListView listView;
    private static PackageManager packageManager;
    private static List<PackageInfo> packageInfoList;
    private static ArrayList<UserAppObject> userPackageInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracked_apps);
        userPackageInfoList = new ArrayList<>();
        filterSystemPackage();
        listView = (ListView) findViewById(R.id.listView);
        trackedAppsListAdapter = new TrackedAppListAdapter(TrackedAppsAcitivity.this,R.layout.tracked_apps_list_item,userPackageInfoList);
        listView.setAdapter(trackedAppsListAdapter);
    }

    public void filterSystemPackage()
    {
        packageManager = getPackageManager();
        packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);
        for(int i = 0; i<packageInfoList.size(); ++i)
        {
            PackageInfo packageInfo1 = packageInfoList.get(i);
            if(!(isSystemPackage(packageInfo1)))
            {
                UserAppObject userAppObject = new UserAppObject(packageInfo1,false);
                Log.d(TAG, "check:"+packageManager.getApplicationLabel(userAppObject.getPackageInfo().applicationInfo).toString());
                userPackageInfoList.add(userAppObject);
            }
        }
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
                : false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tracked_apps_acitivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.save_icon_traked_apps)
        {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
