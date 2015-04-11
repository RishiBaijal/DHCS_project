package in.ac.iiitd.dhcs.focus.MainTabs;


import android.content.Context;
import in.ac.iiitd.dhcs.focus.CustomUIClasses.TimerView;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;

import android.content.Context;

import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewFlipper;

import in.ac.iiitd.dhcs.focus.CustomUIClasses.TimerView;
import in.ac.iiitd.dhcs.focus.MainTabActivity;
import in.ac.iiitd.dhcs.focus.R;
import in.ac.iiitd.dhcs.focus.ZenTimer;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ZenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ZenFragment extends Fragment {
    TimerView timerView;
    TimePicker timePicker;
    Button startBtn,stopBtn;
    ZenTimer zenTimer;
    ViewFlipper viewFlipper;
    private static long TICK=60;//milliseconds
    private final static String TAG="ZenFragment";
    public static long zenModeStartedNumber = 0;
    public static long zenModeStoppedNumber = 0;
    public static long zenModeMilliseconds = 0;
    public static long zenModeCompleted = 0;
    public static long setMillis = 0;
    public static long millisOnDistraction = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ZenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ZenFragment newInstance(String param1, String param2) {
        ZenFragment fragment = new ZenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ZenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainTabActivity.zenVisited++;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("zenModeVisited", Context.MODE_PRIVATE);
        SharedPreferences.Editor startEditor = sharedPreferences.edit();
        startEditor.putLong("visitZen", MainTabActivity.zenVisited);
        System.out.println("The number of times zen has been visited is "+MainTabActivity.zenVisited);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflaterView=inflater.inflate(R.layout.fragment_zen, container, false);

//        MainTabActivity.zenVisited++;
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("zenModeVisited", Context.MODE_PRIVATE);
//        SharedPreferences.Editor startEditor = sharedPreferences.edit();
//        startEditor.putLong("visitZen", MainTabActivity.zenVisited);
//        System.out.println("The number of times zen has been visited is "+MainTabActivity.zenVisited);

        viewFlipper=(ViewFlipper)inflaterView.findViewById(R.id.viewFlipper);

        timerView= (TimerView) inflaterView.findViewById(R.id.timerView);
        timerView.setProgress(50);

        timePicker= (TimePicker) inflaterView.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(0);
        timePicker.setCurrentMinute(0);

        setButtons(inflaterView);

        return inflaterView;
    }

//    public void onPause()
//    {
//        super.onPause();
//        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
//        boolean isScreenOn = powerManager.isScreenOn();
//
//        if (!isScreenOn)
//        {
//            System.out.println("The motherfucking screen is locked");
//        }
//        else
//        {
//            System.out.println("The brotherfucking screen is unlocked.");
//            Context context = getApplicationContext();
//            CharSequence text = "The screen is unlocked. You are getting distracted!";
//            int duration = Toast.LENGTH_LONG;
//
//            Toast.makeText(context, text, duration).show();
//        }
//    }



    @Override
    public void onResume(){
        super.onResume();
        MainTabActivity.zenVisited++;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("zenModeVisited", Context.MODE_PRIVATE);
        SharedPreferences.Editor startEditor = sharedPreferences.edit();
        startEditor.putLong("visitZen", MainTabActivity.zenVisited);
        System.out.println("The number of times zen has been visited is "+MainTabActivity.zenVisited);

    }

    private void setButtons(View inflaterView) {
        startBtn=(Button) inflaterView.findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainTabActivity.zenStarted = true;
                long setMillis=(((timePicker.getCurrentHour()*60+timePicker.getCurrentMinute())*60)*1000);

                long[] pattern = {500,500,500,500,500,500,500,500,500};
                MainTabActivity.zenStarted = true;
                MainTabActivity.wasInZen = true;
                zenModeStartedNumber++;
                NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                Intent intent = new Intent(getActivity().getApplicationContext(), MainTabActivity.class);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getActivity().getApplicationContext()).setSmallIcon(R.drawable.abc_cab_background_top_mtrl_alpha).setContentTitle("ZEN MODE STARTED!!").setContentText("Zen mode has started. Do NOT get distracted!");
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity().getApplicationContext());
                stackBuilder.addParentStack(MainTabActivity.class);
                stackBuilder.addNextIntent(intent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                //Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                //mBuilder.setSound(alarmSound);
                mBuilder.setLights(Color.BLUE, 500, 500);
                mBuilder.setVibrate(pattern);
                mBuilder.setAutoCancel(false);
                mBuilder.setOngoing(true);
                mBuilder.setStyle(new NotificationCompat.InboxStyle());
                mBuilder.setContentIntent(resultPendingIntent);
                notificationManager.notify(0, mBuilder.build());


                Context context = getActivity().getApplicationContext();
                CharSequence text = "Zen mode has now been started. Please do not get distracted!!";
                int duration = Toast.LENGTH_LONG;
                Toast.makeText(context, text, duration).show();

                //This block is where we store the values
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("zenModeStarted", Context.MODE_PRIVATE);
                SharedPreferences.Editor startEditor = sharedPreferences.edit();
                startEditor.putLong("startZen", zenModeStartedNumber);
                System.out.println("The value added to startZen is "+zenModeStartedNumber);
                //Use this code to get the preferences back
//                SharedPreferences prefs = this.getSharedPreferences("zenModeStarted", Context.MODE_PRIVATE);
//                int score = prefs.getInt("startZen", 0); //0 is the default value

                //This block is where we store the values
                SharedPreferences prefs = getActivity().getSharedPreferences("millisInZen", Context.MODE_PRIVATE);
                long milli = prefs.getLong("milliZen", zenModeMilliseconds);
                System.out.println("The value retrieved for zenStopped is "+milli);



                SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("completedZen", Context.MODE_PRIVATE);
                SharedPreferences.Editor completedEditor = sharedPreferences2.edit();
                completedEditor.putLong("completeZen", zenModeCompleted);
                System.out.println("The number of times zen mode has been completed is "+zenModeCompleted);


                setMillis=(((timePicker.getCurrentHour()*60+timePicker.getCurrentMinute())*60)*1000);
                if (ZenTimer.timerFinished) {
                    zenModeCompleted++;
                    System.out.println("Zen mode completed.");
                    zenModeMilliseconds += setMillis;
                    SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("millisInZen", Context.MODE_PRIVATE);
                    SharedPreferences.Editor milliEditor = sharedPreferences1.edit();
                    milliEditor.putLong("milliZen", zenModeMilliseconds);
                    System.out.println("The value added to milliseconds is " + zenModeMilliseconds);
                }

                else// This means the user got distracted
                {

                }

                Log.v(TAG, "Set: " + setMillis);
                viewFlipper.showNext();
                zenTimer=new ZenTimer(setMillis,TICK,timerView,viewFlipper);
                zenTimer.start();

            }
        });

        stopBtn=(Button) inflaterView.findViewById(R.id.stopBtn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(zenTimer!=null)
                {
                    MainTabActivity.zenStarted = false;
                    MainTabActivity.wasInZen = false;
                    zenModeStoppedNumber++;
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("stopZenMode", Context.MODE_PRIVATE);
                    SharedPreferences.Editor stopEditor = sharedPreferences.edit();
                    stopEditor.putLong("stopZen", zenModeStoppedNumber);
                    System.out.println("The value added to stopZen is "+zenModeStoppedNumber);

                    millisOnDistraction=(((timePicker.getCurrentHour()*60+timePicker.getCurrentMinute())*60)*1000);
                    System.out.println("Millis on distraction: "+millisOnDistraction);
                    zenModeMilliseconds +=(millisOnDistraction);
                    System.out.println(millisOnDistraction);


                    SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("millisInZen", Context.MODE_PRIVATE);
                    SharedPreferences.Editor milliEditor = sharedPreferences1.edit();
                    milliEditor.putLong("milliZen", zenModeMilliseconds);
                    System.out.println("The value added to milliseconds is "+zenModeMilliseconds);
                    //zenModeCompleted = zenModeStartedNumber - zenModeStoppedNumber - MainTabActivity.zenModeDistracted;
                    zenTimer.cancel();
                    viewFlipper.showNext();
                    timePicker.setCurrentHour(0);
                    timePicker.setCurrentMinute(0);
                }

            }
        });
    }

}
