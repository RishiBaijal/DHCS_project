package in.ac.iiitd.dhcs.focus;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.Toast;
import android.widget.RelativeLayout;

import java.util.Locale;

import in.ac.iiitd.dhcs.focus.MainTabs.ProductivityFragment;
import in.ac.iiitd.dhcs.focus.MainTabs.StatsFragment;
import in.ac.iiitd.dhcs.focus.MainTabs.ZenFragment;
import in.ac.iiitd.dhcs.focus.Service.FocusService;
import in.ac.iiitd.dhcs.focus.Service.ScreenStateReceiver;
import in.ac.iiitd.dhcs.focus.StatsObjects.HelpActivity;


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


    public static String sent = "";
    public static int zenVisited = 0;
    public static int statsVisited = 0;
    public static int productivityVisited = 0;
    public static int appStatsVisited = 0;
    public static int trackedVisited = 0;
    public static long zenActivated = 0;
    public static long zenCompleted = 0;
    public static long millisInZen = 0;


    public static boolean zenStarted = false;
    public static boolean wasInZen = false;
//    public static int zenModeDistracted = 0;

//
//     */
    //public static boolean zenStarted = false;
// Added toast messages

    private static String TAG ="MainTabActivity";
    public static ViewPager mViewPager;
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

//        if (zenStarted == true)
//        {
//            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setContentTitle("ZEN MODE STARTED!").setContentText("Zen mode has now been started. Do not get distracted!!");
//
//            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            mNotificationManager.notify(0, mBuilder.build());
//
//
//            System.out.println("The user has now been notified.");
//        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        boolean isScreenOn = powerManager.isScreenOn();



        if (!isScreenOn)
        {
            System.out.println("The screen is locked");
        }
        else
        {
            if (zenStarted == true) {//if this is executed, the user got distracted

                long[] pattern = {500,500,500,500,500,500,500,500,500};
                if (ZenTimer.timerFinished) {
                    ZenFragment.zenModeCompleted++;

                    Toast.makeText(this.getApplicationContext(), "Zen Mode Completed! Congratulations!", Toast.LENGTH_LONG).show();
                    NotificationManager notificationManager1 = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                    Intent intent1 = new Intent(this.getApplicationContext(), MainTabActivity.class);
                    NotificationCompat.Builder mBuilder1=
                            new NotificationCompat.Builder(this.getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("ZEN MODE COMPLETED.").setContentText("Congratulations!");
                    TaskStackBuilder stackBuilder1 = TaskStackBuilder.create(this.getApplicationContext());
                    stackBuilder1.addParentStack(MainTabActivity.class);
                    stackBuilder1.addNextIntent(intent1);
                    PendingIntent resultPendingIntent1 = stackBuilder1.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder1.setLights(Color.BLUE, 500, 500);
                    mBuilder1.setVibrate(pattern);
                    mBuilder1.setAutoCancel(false);
                    mBuilder1.setOngoing(false);
                    mBuilder1.setStyle(new NotificationCompat.InboxStyle());
                    mBuilder1.setContentIntent(resultPendingIntent1);
                    notificationManager1.notify(0, mBuilder1.build());



                    SharedPreferences sharedPreferences2 = this.getSharedPreferences("completedZen", Context.MODE_PRIVATE);
                    SharedPreferences.Editor completedEditor = sharedPreferences2.edit();
                    completedEditor.putLong("completeZen", ZenFragment.zenModeCompleted);
                    MainTabActivity.sent += "The number of times zen mode has been completed is : "+ZenFragment.zenModeCompleted + "\n";
                    System.out.println("The number of times zen mode has been completed is : "+ ZenFragment.zenModeCompleted);
                    MainTabActivity.zenCompleted = ZenFragment.zenModeCompleted;

                    System.out.println("Zen mode completed.");
                    ZenFragment.zenModeMilliseconds += ZenFragment.setMillis;
                    SharedPreferences sharedPreferences1 = this.getSharedPreferences("millisInZen", Context.MODE_PRIVATE);
                    SharedPreferences.Editor milliEditor = sharedPreferences1.edit();
                    milliEditor.putLong("milliZen", ZenFragment.zenModeMilliseconds);
                    System.out.println("The value added to milliseconds is " + ZenFragment.zenModeMilliseconds);
                    MainTabActivity.millisInZen = ZenFragment.zenModeMilliseconds;
                    ZenTimer.timerFinished = false;
                    zenStarted = false;
                }

                else {

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    Intent intent = new Intent(getApplicationContext(), MainTabActivity.class);
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("ZEN MODE OVER!!").setContentText("Seems like you got distracted. Better luck next time!!");
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                    stackBuilder.addParentStack(MainTabActivity.class);
                    stackBuilder.addNextIntent(intent);
                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setAutoCancel(false);
                    //Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    //mBuilder.setSound(alarmSound);
                    mBuilder.setLights(Color.RED, 500, 500);
                    mBuilder.setVibrate(pattern);
                    mBuilder.setStyle(new NotificationCompat.InboxStyle());
                    //mBuilder.setOngoing(true);
                    mBuilder.setContentIntent(resultPendingIntent);
                    notificationManager.notify(0, mBuilder.build());

                    System.out.println("The value of zenStarted is " + zenStarted);
                    System.out.println("The screen is unlocked.");
//                Context context = getApplicationContext();
//                CharSequence text = "The screen is unlocked. You are getting distracted!";
//                int duration = Toast.LENGTH_LONG;
                    //zenModeDistracted++;
                    zenStarted = false;
                }


             //   Toast.makeText(context, text, duration).show();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (wasInZen)
        {
            mViewPager.setCurrentItem(0, true);// switch to zen
        }
        else
        {
            mViewPager.setCurrentItem(1);
        }
        wasInZen = false;
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

        if (id == R.id.action_tracked_apps)
        {
            Intent intent = new Intent(this, TrackedAppsAcitivity.class);
            this.startActivity(intent);
        }

        else if (id == R.id.about_this_app)
        {
            Intent intent = new Intent(this, HelpActivity.class);
            this.startActivity(intent);
        }

        else if (id == R.id.action_share_data)
        {
            sent = "";

            ZenFragment zenFragment = new ZenFragment();
//            SharedPreferences prefs = zenFragment.getActivity().getSharedPreferences("zenModeStarted", Context.MODE_PRIVATE);
  //          long zenStart = prefs.getLong("startZen", ZenFragment.zenModeStartedNumber);
    //        long zenComplete = prefs.getLong("completeZen", ZenFragment.zenModeCompleted);
            sent = "Number of times Zen Mode was visited : " + MainTabActivity.zenVisited + "\n";
            sent += "Number of times Productivity tab was visited : " + MainTabActivity.productivityVisited + "\n";
            sent += "Number of times Statistics tab was visited : " + MainTabActivity.statsVisited + "\n";
            sent += "Number of times the individual app statistics were accessed : " + MainTabActivity.appStatsVisited + "\n";
            sent += "Number of times the Tracked apps were viewed : " + MainTabActivity.trackedVisited + "\n";
            sent += "Number of times Zen Mode was activated : " + MainTabActivity.zenActivated + "\n";
            sent += "Number of times Zen Mode was completed : " + MainTabActivity.zenCompleted + "\n";
            sent += "Number of milli-seconds spent in Zen Mode : " + MainTabActivity.millisInZen + "\n";
        //    sent += "Number of times Zen Mode was finished : " + zenComplete;
//            long mod = zenComplete % 100;
//            if (mod == 0)
//            {
//                sent += "Congratulations! You have completed zen mode " + mod + " times. Awesome job!";
//            }
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, sent);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

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
                    System.out.println("Number of times stats fragment has been visited = "+MainTabActivity.statsVisited);
                    break;
                case 1:
                    fragment=new ProductivityFragment();
                    System.out.println("Number of times the productivity fragment has been visited = "+MainTabActivity.productivityVisited);
                    break;
                case 0:
                    fragment=new ZenFragment();
                    System.out.println("Number of times the zen fragment has been visited = "+MainTabActivity.zenVisited);
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
        Intent myIntentTrackedApp = new Intent(this, TrackedAppsAcitivity.class);

        if(runFirst){
//            startActivity(myIntent);
            startActivity(myIntentTrackedApp);
            View view = (View) findViewById(R.id.overlay_view);
            view.bringToFront();
            view.setVisibility(View.VISIBLE);
        }else {

        }
        mPreferences.edit().putBoolean(Utils.KEY_IS_FIRST_RUN,false).commit();
    }

    public void nextOverlay(View v){
//        setContentView(R.layout.fragment_productivity);
        v = (View) findViewById(R.id.overlay_view);
        v.setVisibility(View.GONE);
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
