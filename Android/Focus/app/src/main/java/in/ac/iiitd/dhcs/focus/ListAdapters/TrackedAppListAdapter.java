package in.ac.iiitd.dhcs.focus.ListAdapters;

import android.content.Context;
import android.content.pm.PackageManager;
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
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import in.ac.iiitd.dhcs.focus.Objects.TrackedAppObject;
import in.ac.iiitd.dhcs.focus.Objects.UserAppObject;
import in.ac.iiitd.dhcs.focus.R;

/**
 * Created by Shubham on 23 Mar 15.
 */
public class TrackedAppListAdapter extends ArrayAdapter<UserAppObject> {

    private static String TAG = "TrackedAppListAdapter";

    private PackageManager packageManager;
    private Context context;
    private ArrayList<UserAppObject> userAppObjectList = new ArrayList<>();
    public static List<TrackedAppObject> trackedAppObjectlist = new CopyOnWriteArrayList<>();
    private UserAppObject userAppObject = new UserAppObject();

    private float DEFAULT_PRODUCTIVITY_SCORE = (float) 5.0;

    public TrackedAppListAdapter(Context context, int resource, ArrayList<UserAppObject> list)
    {
        super(context, resource, list);
        this.context = context;
        this.userAppObjectList = list;
        packageManager = context.getPackageManager();
        Log.d(TAG,"userObjectListSize"+userAppObjectList.size());
    }

    public void addTrackedApp(String appName, float appProductivity)
    {
        TrackedAppObject trackedAppObject = new TrackedAppObject(appName,appProductivity);
        trackedAppObjectlist.add(trackedAppObject);
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
        Log.d(TAG,"list size:"+String.valueOf(trackedAppObjectlist.size()));
        Log.d(TAG,"wasDeleted:"+String.valueOf(wasDeleted));
    }

    public void updateProductivity(String appName, float productivityValue)
    {
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

        seekBarProductivity.setProgress(5);
        textViewSeekValue.setText(String.valueOf(seekBarProductivity.getProgress()));
        textViewAppName.setText(packageManager.getApplicationLabel(userAppObject.getPackageInfo().applicationInfo).toString());
        imageViewAppIcon.setImageDrawable(packageManager.getApplicationIcon(userAppObject.getPackageInfo().applicationInfo));
        checkBoxTrack.setChecked(userAppObject.getIsChecked()); //to be extracted from DB

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


}
