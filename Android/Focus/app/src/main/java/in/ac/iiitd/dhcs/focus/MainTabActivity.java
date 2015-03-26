package in.ac.iiitd.dhcs.focus;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;


import java.util.Locale;

import in.ac.iiitd.dhcs.focus.MainTabs.ProductivityFragment;
import in.ac.iiitd.dhcs.focus.MainTabs.StatsFragment;
import in.ac.iiitd.dhcs.focus.MainTabs.ZenFragment;
import in.ac.iiitd.dhcs.focus.Service.FocusService;
import in.ac.iiitd.dhcs.focus.Service.ScreenStateReceiver;


public class MainTabActivity extends ActionBarActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private static String TAG ="MainTabActivity";
    ViewPager mViewPager;
    public SharedPreferences mPreferences;
    boolean showTut;
    Context mContext;
    RelativeLayout mOverlayLayout;
    Context context;
    private static ScreenStateReceiver mScreenStateReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        context = getApplicationContext();
        mPreferences = getSharedPreferences(Utils.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
       // mOverlayLayout=(RelativeLayout)findViewById(R.id.OverlayLayout);
        //check if this is the first run and show tutorial if so

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);

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

       // ViewPager viewPager = (ViewPager) findViewById(R.id.overlayPager);
       // ImageAdapter adapter = new ImageAdapter(getApplicationContext());
       // viewPager.setAdapter(adapter);

        // showActivityOverlay();
        mScreenStateReceiver = new ScreenStateReceiver();
        registerReceiver();
        startFocusService();
        checkPrefsAndShowOverlay();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewPager.setCurrentItem(1);
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
    private void checkPrefsAndShowOverlay() {
//check if first run and display overlays

        boolean runFirst=  !mPreferences.contains(Utils.KEY_IS_FIRST_RUN);
// use a default value using new Date()
        Intent myIntent = new Intent(this, OverlayActivity.class);

        if(runFirst){
            startActivity(myIntent);
        }else {

        }
        mPreferences.edit().putBoolean(Utils.KEY_IS_FIRST_RUN,false).commit();
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
    public void onBackPressed() {
    // TODO Auto-generated method stub
    moveTaskToBack(true);
    }
}
