package in.ac.iiitd.dhcs.focus;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import in.ac.iiitd.dhcs.focus.Database.DbContract;
import in.ac.iiitd.dhcs.focus.Database.FocusDbHelper;
import in.ac.iiitd.dhcs.focus.ListAdapters.TrackedAppListAdapter;
import in.ac.iiitd.dhcs.focus.Objects.UserAppObject;


public class TrackedAppsAcitivity extends ActionBarActivity {

    private static String TAG = "TrackedAppsActivity";

    private static TrackedAppListAdapter trackedAppsListAdapter;
    private static ListView listView;
    private static PackageManager packageManager;
    private static List<PackageInfo> packageInfoList;
    private static ArrayList<UserAppObject> userPackageInfoList;
    FocusDbHelper dbs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracked_apps);
        userPackageInfoList = new ArrayList<UserAppObject>();
        filterSystemPackage();
        dbs = new FocusDbHelper(getApplicationContext());
        listView = (ListView) findViewById(R.id.listView);
        trackedAppsListAdapter = new TrackedAppListAdapter(TrackedAppsAcitivity.this,R.layout.tracked_apps_list_item,userPackageInfoList);
        listView.setAdapter(trackedAppsListAdapter);


    }

    protected void onResume()
    {
        super.onResume();
        MainTabActivity.trackedVisited++;
        SharedPreferences sharedPreferences = this.getSharedPreferences("trackedVisited", Context.MODE_PRIVATE);
        SharedPreferences.Editor startEditor = sharedPreferences.edit();
        startEditor.putLong("visitTracked", MainTabActivity.trackedVisited);
        System.out.println("The number of times tracked apps screen has been visited is "+MainTabActivity.trackedVisited);
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
            updateProductivityDB();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void updateProductivityDB(){
        SQLiteDatabase db = dbs.getWritableDatabase();

        for(UserAppObject obj : TrackedAppListAdapter.userAppObjectList) {
            if(obj.getIsChecked()){
            String appName = packageManager.getApplicationLabel(obj.getPackageInfo().applicationInfo).toString();
            Float Score = obj.getProdscore();
            //TrackedAppListAdapter.trackedapps.put(AppName, Score);
            Log.v(TAG, "Updating" + appName + " : " + Score);
            String sql = "select " + DbContract.TrackedAppEntry._ID + " from '" + DbContract.TrackedAppEntry.TABLE_NAME + "'" +
                    " where " + DbContract.TrackedAppEntry.APP_NAME + " LIKE '" + appName + "'";
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();
            String rowid = cursor.getString(cursor.getColumnIndex(DbContract.TrackedAppEntry._ID));
            ContentValues values = new ContentValues();
            values.put(DbContract.TrackedAppEntry.PRODUCTIVITY_SCORE, Score);
            db.update(DbContract.TrackedAppEntry.TABLE_NAME, values, DbContract.TrackedAppEntry._ID + "=" + rowid, null);
        }

        }

        /*
        for(TrackedAppObject obj : TrackedAppListAdapter.trackedAppObjectlist){
            String AppName = obj.getName();
            Float Score = obj.getReading();
            TrackedAppListAdapter.trackedapps.put(AppName,Score);
            Log.v(TAG,"Updating" + AppName + " : " + Score);
            String sql = "select " + DbContract.TrackedAppEntry._ID + " from '" + DbContract.TrackedAppEntry.TABLE_NAME + "'" +
                    " where " + DbContract.TrackedAppEntry.APP_NAME + " LIKE '" + AppName + "'";
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();
            String rowid = cursor.getString(cursor.getColumnIndex(DbContract.TrackedAppEntry._ID));
            ContentValues values = new ContentValues();
            values.put(DbContract.TrackedAppEntry.PRODUCTIVITY_SCORE,Score);
            db.update(DbContract.TrackedAppEntry.TABLE_NAME, values, DbContract.TrackedAppEntry._ID + "=" + rowid, null);
        }
    */
        db.close();
    }
}
