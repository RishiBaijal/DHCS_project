package in.ac.iiitd.dhcs.focus.ListAdapters;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import in.ac.iiitd.dhcs.focus.Common.CommonUtils;
import in.ac.iiitd.dhcs.focus.Database.DbContract;
import in.ac.iiitd.dhcs.focus.Database.FocusDbHelper;
import in.ac.iiitd.dhcs.focus.Objects.TrackedAppObject;
import in.ac.iiitd.dhcs.focus.Objects.UserAppObject;
import in.ac.iiitd.dhcs.focus.R;

/**
 * Created by Shubham on 23 Mar 15.
 */
public class TrackedAppListAdapter extends ArrayAdapter<UserAppObject> {

    private static String TAG = "TrackedAppListAdapter";

    FocusDbHelper dbs;
    private PackageManager packageManager;
    private Context context;
    private ArrayList<UserAppObject> userAppObjectList = new ArrayList<>();
    public static List<TrackedAppObject> trackedAppObjectlist = new CopyOnWriteArrayList<>();
    public static HashMap<String,Float> trackedapps = new HashMap<String,Float>();
    private UserAppObject userAppObject = new UserAppObject();

    private float DEFAULT_PRODUCTIVITY_SCORE = (float) 5.0;

    public TrackedAppListAdapter(Context context, int resource, ArrayList<UserAppObject> list)
    {
        super(context, resource, list);
        this.context = context;
        this.userAppObjectList = list;
        packageManager = context.getPackageManager();
        dbs = new FocusDbHelper(context);
        Log.d(TAG,"userObjectListSize"+userAppObjectList.size());
    }

    public void addTrackedApp(String appName, float appProductivity)
    {
        TrackedAppObject trackedAppObject = new TrackedAppObject(appName,appProductivity);
        trackedAppObjectlist.add(trackedAppObject);
        trackedapps.put(appName,appProductivity);
        insertData(appName,appProductivity);
        Log.d(TAG, "appName:" + appName + " ProductivityScore:" + appProductivity);
        for(TrackedAppObject trackedAppObject1:trackedAppObjectlist)
            Log.d("trackedObjectList",trackedAppObject1.getName());
    }

    public void removeTrackedApp(String appName)
    {
        boolean wasDeleted = false;
        Iterator<TrackedAppObject> iterator = trackedAppObjectlist.iterator();
        while (iterator.hasNext())
        {
            TrackedAppObject objectToRemoved = iterator.next();
            if(objectToRemoved.getName().equals(appName))
                wasDeleted = trackedAppObjectlist.remove(objectToRemoved);
        }
        trackedapps.remove(appName);
        removeApp(appName);
        Log.d(TAG,"list size:"+String.valueOf(trackedAppObjectlist.size()));
        Log.d(TAG,"wasDeleted:"+String.valueOf(wasDeleted));
    }

    public void updateProductivity(String appName, float productivityValue)
    {
        trackedapps.put(appName,productivityValue);
        for(int i = 0; i<trackedAppObjectlist.size(); ++i)
        {
            TrackedAppObject objectToBeUpdated = trackedAppObjectlist.get(i);
            if(objectToBeUpdated.getName().equals(appName))
                objectToBeUpdated.putReading(productivityValue);
        }
    }

    @Override
    public int getCount() {
        return userAppObjectList.size();
    }

    @Override
    public UserAppObject getItem(int position) {
        return userAppObjectList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        //getting already tracking apps list from db//
        getList();

        View view = convertView;
        userAppObject = userAppObjectList.get(position);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.tracked_apps_list_item, parent, false);

        final TextView textViewAppName = (TextView) view.findViewById(R.id.app_name_text);
        ImageView imageViewAppIcon = (ImageView) view.findViewById(R.id.app_icon);
        final CheckBox checkBoxTrack = (CheckBox) view.findViewById(R.id.app_tracking_check);
        final SeekBar seekBarProductivity = (SeekBar) view.findViewById(R.id.app_value_seek);
        final TextView textViewSeekValue = (TextView) view.findViewById(R.id.app_seek_value_text);

        String appName = packageManager.getApplicationLabel(userAppObject.getPackageInfo().applicationInfo).toString();
        boolean istracked = trackedapps.containsKey(appName);

        if(istracked){
            checkBoxTrack.setChecked(true);
            seekBarProductivity.setProgress(Math.round(trackedapps.get(appName)));
        }
        else{
            checkBoxTrack.setChecked(false);
            seekBarProductivity.setProgress(5);
        }

        textViewSeekValue.setText(String.valueOf(seekBarProductivity.getProgress()));
        textViewAppName.setText(appName);
        imageViewAppIcon.setImageDrawable(packageManager.getApplicationIcon(userAppObject.getPackageInfo().applicationInfo));

        checkBoxTrack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG,"position:"+position);
                userAppObjectList.get(position).setChecked(isChecked);
                buttonView.setChecked(userAppObjectList.get(position).getIsChecked());
                String appName = textViewAppName.getText().toString();
                float appProductivity = (float) seekBarProductivity.getProgress();
                if(isChecked)
                {
                    addTrackedApp(appName, appProductivity);
                }
                else
                {
                    removeTrackedApp(appName);
                }
            }
        });

        seekBarProductivity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                    Log.d(TAG,String.valueOf(progress));
                boolean checkedValue = userAppObjectList.get(position).getIsChecked();
                textViewSeekValue.setText(String.valueOf(progress));
                if(checkedValue)
                {
                    float productivity = (float) progress;
                    String appName = textViewAppName.getText().toString();
                    updateProductivity(appName,productivity);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return view;
    }

    public void insertData(String AppName,float Score) {
        SQLiteDatabase db = dbs.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbContract.TrackedAppEntry.APP_NAME, AppName);
        values.put(DbContract.TrackedAppEntry.PRODUCTIVITY_SCORE, Score);
        // Inserting 1Row
        db.insert(DbContract.TrackedAppEntry.TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    public void removeApp(String AppName){
        SQLiteDatabase db = dbs.getWritableDatabase();
        String sql = "select " + DbContract.TrackedAppEntry._ID + " from '" + DbContract.TrackedAppEntry.TABLE_NAME + "'" +
                " where " + DbContract.TrackedAppEntry.APP_NAME + " LIKE '" + AppName + "'";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        String rowid = cursor.getString(cursor.getColumnIndex(DbContract.TrackedAppEntry._ID));
        cursor.close();
        // Deleting App from tracking db
        db.delete(DbContract.TrackedAppEntry.TABLE_NAME, DbContract.TrackedAppEntry._ID + "=" + rowid, null);
        db.close(); // Closing database connection
    }

    public void getList(){
        SQLiteDatabase db = dbs.getWritableDatabase();
        String sql = "select * from '" + DbContract.TrackedAppEntry.TABLE_NAME + "'";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
        }

        trackedapps.clear();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
        String appName = cursor.getString(cursor.getColumnIndex(DbContract.TrackedAppEntry.APP_NAME));
        Float Score = cursor.getFloat(cursor.getColumnIndex(DbContract.TrackedAppEntry.PRODUCTIVITY_SCORE));
        trackedapps.put(appName,Score);
        }
        cursor.close();
        db.close(); // Closing database connection
    }

}
