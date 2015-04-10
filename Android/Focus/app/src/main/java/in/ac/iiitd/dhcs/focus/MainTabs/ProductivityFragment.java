package in.ac.iiitd.dhcs.focus.MainTabs;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import in.ac.iiitd.dhcs.focus.AppStatsActivity;
import in.ac.iiitd.dhcs.focus.Common.CommonUtils;
import in.ac.iiitd.dhcs.focus.CustomUIClasses.AppDistributionView;
import in.ac.iiitd.dhcs.focus.CustomUIClasses.MeterView;
import in.ac.iiitd.dhcs.focus.Database.DbContract;
import in.ac.iiitd.dhcs.focus.Database.FocusDbHelper;
import in.ac.iiitd.dhcs.focus.Objects.ProductivityObject;
import in.ac.iiitd.dhcs.focus.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductivityFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static String TAG="ProductivityFragment";
    public static ArrayList<ProductivityObject> ProductivityList;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView textViewGoal;
    LinearLayout ll;
    FocusDbHelper dbs ;
    MeterView productivityMeterView;
    private Context context;
    private ArrayList<AppDistributionView> appDistributionList = new ArrayList<AppDistributionView>();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductivityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductivityFragment newInstance(String param1, String param2) {
        ProductivityFragment fragment = new ProductivityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ProductivityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        dbs = new FocusDbHelper(context);
    }

    @Override
    public void onResume(){
        super.onResume();

        if(CommonUtils.ProductivityScore==0L){
            updateProductivity();
        }

        long timeInMillis = System.currentTimeMillis();
        if(getcount(CommonUtils.unixTimestampToDate(timeInMillis))<1) {
            Log.v(TAG,"DateCountforProd<0");
            CommonUtils.ProductivityGoal = 50L;
            textViewGoal.setText("50%");
        }
        else if(CommonUtils.ProductivityGoal==0L){
            getProdGoal();
            Log.v(TAG,"DateCountforProd=0"+String.valueOf(getcount(CommonUtils.unixTimestampToDate(timeInMillis))));
        }

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something here
                removeViews();
                updateList();
            }
        }, 700);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflaterView=inflater.inflate(R.layout.fragment_productivity, container, false);
        productivityMeterView=(MeterView)inflaterView.findViewById(R.id.productivityMeterView);
        ll=(LinearLayout)inflaterView.findViewById(R.id.productivityLinearLayout);
        textViewGoal = (TextView) ll.findViewById(R.id.textView3);
        return inflaterView;
    }

    private void addAppDistribution(final String name,Drawable icon,long duration,float progress){
        AppDistributionView app1 = new AppDistributionView(getActivity(), null);
        ll.addView(app1);
        app1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),AppStatsActivity.class);
                intent.putExtra("appName",name);
                startActivity(intent);
            }
        });
        app1.setProgress(progress);
        app1.setDuration(duration);
        app1.setIcon(icon);
        app1.setAppName(name);
        appDistributionList.add(app1);
    }

    private void removeViews() {
        if(!appDistributionList.isEmpty()){
            for(AppDistributionView app1:appDistributionList)
                ll.removeView(app1);
            appDistributionList.clear();
        }
    }

    public void setMainProductivty(){
        productivityMeterView.setProgress(CommonUtils.ProductivityScore);
        productivityMeterView.setTarget((float)CommonUtils.ProductivityGoal);
        productivityMeterView.setProgressValue(CommonUtils.TotalProductivity);
    }
    public void updateList(){
        SQLiteDatabase db = dbs.getWritableDatabase();
        ProductivityList = new ArrayList<ProductivityObject>();
        PackageManager pm = context.getPackageManager();
        long timeInMillis = System.currentTimeMillis();
        String todaydate = CommonUtils.unixTimestampToDate(timeInMillis);
        CommonUtils.TotalProductivity=0L;
        String sql = "select * from '"+ DbContract.ProductivityEntry.TABLE_NAME+"'" +
                " where "+ DbContract.ProductivityEntry.TRACKING_DATE+" LIKE '"+todaydate+"'"  ;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
        }
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            ProductivityObject obj = new ProductivityObject();
            if(!cursor.getString(1).equalsIgnoreCase("Unknown")){
            obj.putName(cursor.getString(1));
            obj.putPackageName(cursor.getString(2));
            obj.putUsageDuration(cursor.getLong(4));
            obj.putProductivityDuration(cursor.getLong(5));
            try {
                obj.putAppIcon(context.getPackageManager().getApplicationIcon(cursor.getString(2)));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            CommonUtils.TotalProductivity+= cursor.getLong(5);
            ProductivityList.add(obj);}

            else{
                continue;
            }
        }
        cursor.close();
        setMainProductivty();
        Log.v(TAG, String.valueOf(ProductivityList.size()));

        for(ProductivityObject obj : ProductivityList){

            float progress;
            if(CommonUtils.TotalProductivity==0L){
                progress=0;
            }
            else{
             progress=(float)(obj.getProductivityDuration()*100) / (float)(CommonUtils.TotalProductivity);
            }
            Log.v(TAG, obj.getName()+" " + obj.getUsageDuration() + " " + obj.getProductivityDuration());
            addAppDistribution(obj.getName(),obj.getAppIcon(),obj.getProductivityDuration(),progress);
        }
    }


    public int getcount(String Date) {

        SQLiteDatabase db = dbs.getWritableDatabase();

        String sql = "select count(*) from '" + DbContract.ProductivityEntry.TABLE_NAME + "' where " +
                DbContract.ProductivityEntry.TRACKING_DATE + " < '" + Date + "'";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        int length = cursor.getInt(0);
        cursor.close();
        db.close();
        return length;
    }

    public void getProdGoal() {
        SQLiteDatabase db = dbs.getWritableDatabase();
        long timeInMillis = System.currentTimeMillis();
        String todaydate = CommonUtils.unixTimestampToDate(timeInMillis);
        CommonUtils.ProductivityGoal = CommonUtils.TotalDuration = CommonUtils.TotalProductivity = 0L;

        String sql = "select " + DbContract.ProductivityEntry.USAGE_DURATION + "," + DbContract.ProductivityEntry.PRODUCTIVE_DURATION + " from '" + DbContract.ProductivityEntry.TABLE_NAME + "'" +
                " where " + DbContract.ProductivityEntry.TRACKING_DATE + " < '" + todaydate + "'";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
        }
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            CommonUtils.TotalDuration += cursor.getLong(cursor.getColumnIndex(DbContract.ProductivityEntry.USAGE_DURATION));
            CommonUtils.TotalProductivity += cursor.getLong(cursor.getColumnIndex(DbContract.ProductivityEntry.PRODUCTIVE_DURATION));
        }
        cursor.close();
        CommonUtils.ProductivityGoal = (long) (((float) CommonUtils.TotalProductivity / (float) CommonUtils.TotalDuration) * 100);
        textViewGoal.setText(String.valueOf((int)CommonUtils.ProductivityGoal) +"%");
        Log.v(TAG, CommonUtils.TotalProductivity + " " + CommonUtils.TotalDuration + " " + 100 * ((float) CommonUtils.TotalProductivity / (float) CommonUtils.TotalDuration));
        db.close();
    }


    public void updateProductivity() {
        SQLiteDatabase db = dbs.getWritableDatabase();
        long timeInMillis = System.currentTimeMillis();
        String todaydate = CommonUtils.unixTimestampToDate(timeInMillis);
        CommonUtils.ProductivityScore = CommonUtils.TotalDuration = CommonUtils.TotalProductivity = 0L;

        String sql = "select " + DbContract.ProductivityEntry.USAGE_DURATION + "," + DbContract.ProductivityEntry.PRODUCTIVE_DURATION + " from '" + DbContract.ProductivityEntry.TABLE_NAME + "'" +
                " where " + DbContract.ProductivityEntry.TRACKING_DATE + " LIKE '" + todaydate + "'";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
        }
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            CommonUtils.TotalDuration += cursor.getLong(0);
            CommonUtils.TotalProductivity += cursor.getLong(1);
        }
        cursor.close();
        CommonUtils.ProductivityScore = (long) (((float) CommonUtils.TotalProductivity / (float) CommonUtils.TotalDuration) * 100);
        Log.v(TAG, CommonUtils.TotalProductivity + " " + CommonUtils.TotalDuration + " " + 100 * ((float) CommonUtils.TotalProductivity / (float) CommonUtils.TotalDuration));

        db.close();
    }
}
