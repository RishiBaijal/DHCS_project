package in.ac.iiitd.dhcs.focus;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import in.ac.iiitd.dhcs.focus.Common.CommonUtils;
import in.ac.iiitd.dhcs.focus.Database.DbContract;
import in.ac.iiitd.dhcs.focus.Database.FocusDbHelper;
import in.ac.iiitd.dhcs.focus.MainTabs.ProductivityFragment;
import in.ac.iiitd.dhcs.focus.MainTabs.StatsFragment;
import in.ac.iiitd.dhcs.focus.MainTabs.ZenFragment;
import in.ac.iiitd.dhcs.focus.Objects.ProductivityObject;
import in.ac.iiitd.dhcs.focus.Service.FocusService;
import in.ac.iiitd.dhcs.focus.Service.ScreenStateReceiver;


public class MainTabActivity extends ActionBarActivity implements ActionBar.TabListener {

    public static ArrayList<ProductivityObject> ProductivityList ;
    private static String TAG ="MainTabActivity";
    FocusDbHelper dbs ;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    Context context;
    private static ScreenStateReceiver mScreenStateReceiver;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);

        context = getApplicationContext();
        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        mViewPager.setCurrentItem(1);

        mScreenStateReceiver = new ScreenStateReceiver();
        registerReceiver();
        startFocusService();
        dbs = new FocusDbHelper(context);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_tab, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            Fragment fragment=null;
            switch(position){
                case 2:
                    fragment=new StatsFragment();
                    break;
                case 1:
                    fragment=new ProductivityFragment();
                    break;
                case 0:
                    fragment=new ZenFragment();
                    break;

            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }


    private void startFocusService(){
        Intent intent = new Intent(context, FocusService.class);
        startService(intent);
    }

    private void stopFocusService(){
        Intent intent = new Intent(context, FocusService.class);
        stopService(intent);
    }
    private void registerReceiver(){
        IntentFilter screenStateFilter = new IntentFilter();
        screenStateFilter.addAction(Intent.ACTION_SCREEN_ON);
        screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF);

        if (Build.VERSION.SDK_INT >= 17) {
            screenStateFilter.addAction(Intent.ACTION_DREAMING_STARTED);
            screenStateFilter.addAction(Intent.ACTION_DREAMING_STOPPED);
        }
        registerReceiver(mScreenStateReceiver, screenStateFilter);
    }

    @Override
    public void onResume(){
        super.onResume();
        updateList();
    }

    public void updateList(){
        SQLiteDatabase db = dbs.getWritableDatabase();
        ProductivityList = new ArrayList<ProductivityObject>();
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
            obj.putName(cursor.getString(1));
            obj.putPackageName(cursor.getString(2));
            obj.putUsageDuration(cursor.getLong(4));
            obj.putProductivityDuration(cursor.getLong(5));
            CommonUtils.TotalProductivity +=cursor.getLong(5);
            ProductivityList.add(obj);
        }
        cursor.close();

        Log.v(TAG,String.valueOf(ProductivityList.size()));

        for(ProductivityObject obj : ProductivityList){
            Log.v(TAG,obj.getName()+" " + obj.getUsageDuration()+"s "+ obj.getUsageDuration()/CommonUtils.TotalProductivity +"%");
        }
    }
}
