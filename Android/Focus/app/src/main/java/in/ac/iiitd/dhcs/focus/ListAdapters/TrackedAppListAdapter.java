package in.ac.iiitd.dhcs.focus.ListAdapters;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
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
import java.util.List;

import in.ac.iiitd.dhcs.focus.Objects.TrackedAppObject;
import in.ac.iiitd.dhcs.focus.R;

/**
 * Created by Shubham on 23 Mar 15.
 */
public class TrackedAppListAdapter extends ArrayAdapter<PackageInfo> {

    private static String TAG = "TrackedAppListAdapter";

    private Context context;
    private PackageManager packageManager;
    private List<PackageInfo> packageInfoList;
    private ArrayList<PackageInfo> userPackageInfoList = new ArrayList<>();
    public static ArrayList<TrackedAppObject> trackedAppObjectlist = new ArrayList<>();
    private PackageInfo packageInfo;

    private float DEFAULT_PRODUCTIVITY_SCORE = (float) 5.0;

    public TrackedAppListAdapter(Context context, int resource)
    {
        super(context, resource);
        this.context = context;
        filterSystemPackage();
    }

    public void filterSystemPackage()
    {
        packageManager = context.getPackageManager();
        packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);
        for(PackageInfo packageInfo1 : packageInfoList)
        {
            if(!(isSystemPackage(packageInfo1)))
            {
                userPackageInfoList.add(packageInfo1);
                TrackedAppObject trackedAppObject = new TrackedAppObject(packageManager.getApplicationLabel(packageInfo1.applicationInfo).toString(),DEFAULT_PRODUCTIVITY_SCORE);
                trackedAppObjectlist.add(trackedAppObject);
            }
        }
    }

    static class ViewHolder
    {
        ImageView imageViewAppIcon;
        TextView textViewAppName;
        CheckBox checkBoxTrack;
        SeekBar seekBarProductivity;
        TextView textViewSeekValue;
    }

    @Override
    public int getCount() {
        return userPackageInfoList.size();
    }

    @Override
    public PackageInfo getItem(int position) {
        return userPackageInfoList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        packageInfo = userPackageInfoList.get(position);

        if(view==null)
        {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.tracked_apps_list_item, parent, false);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.textViewAppName = (TextView) view.findViewById(R.id.app_name_text);
            viewHolder.imageViewAppIcon = (ImageView) view.findViewById(R.id.app_icon);
            viewHolder.checkBoxTrack = (CheckBox) view.findViewById(R.id.app_tracking_check);
            viewHolder.seekBarProductivity = (SeekBar) view.findViewById(R.id.app_value_seek);
            viewHolder.seekBarProductivity.setProgress(5);
            viewHolder.textViewSeekValue = (TextView) view.findViewById(R.id.app_seek_value_text);
            viewHolder.textViewSeekValue.setText(String.valueOf(viewHolder.seekBarProductivity.getProgress()));

            viewHolder.checkBoxTrack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.d(TAG,"button"+buttonView.getId()+"is:"+isChecked);
                    if(isChecked)
                    {
                        String appName = packageManager.getApplicationLabel(packageInfoList.get(position).applicationInfo).toString();
                        float appProductivity = (float) viewHolder.seekBarProductivity.getProgress();
                        TrackedAppObject trackedAppObject = new TrackedAppObject(appName,appProductivity);
                        trackedAppObjectlist.add(trackedAppObject);
                        for(TrackedAppObject trackedAppObject1:trackedAppObjectlist)
                            Log.d("trackedObjectList",trackedAppObject1.getName());
                    }
                }
            });

            viewHolder.seekBarProductivity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                    Log.d(TAG,String.valueOf(progress));
                    viewHolder.textViewSeekValue.setText(String.valueOf(progress));

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            view.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();

        Log.d(TAG,trackedAppObjectlist.get(position).getName());

        holder.textViewAppName.setText(packageManager.getApplicationLabel(packageInfo.applicationInfo).toString());
        holder.imageViewAppIcon.setImageDrawable(packageManager.getApplicationIcon(packageInfo.applicationInfo));

        return view;
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
                : false;
    }
}
