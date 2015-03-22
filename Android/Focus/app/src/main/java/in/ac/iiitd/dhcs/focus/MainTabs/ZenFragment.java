package in.ac.iiitd.dhcs.focus.MainTabs;


import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import android.app.Activity;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import java.util.concurrent.TimeUnit;
import java.util.*;

import in.ac.iiitd.dhcs.focus.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ZenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ZenFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    EditText timePicker1;

    View inflateView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    Button btnStart, btnStop;
    TextView textViewTime;

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
       // setContentView(R.layout.fragment_zen);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);


        }



    }

    ScreenBroadcastReceiver receiver = new ScreenBroadcastReceiver();

    public void onResume()
    {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter (Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        getActivity().registerReceiver(receiver, intentFilter);


    }

    public void onPause()
    {
        super.onPause();
        //KeyguardManager keyguardManager = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
       // KeyguardManager myKM = (KeyguardManager) Context.getSystemService(Context.KEYGUARD_SERVICE);
       // boolean isScreenOn = powerManager.isScreenOn();
       // getActivity().unregisterReceiver(receiver);
        //ScreenBroadcastReceiver obj=new ScreenBroadcastReceiver();
        //boolean found=obj.isScreenOn();
        //if (!(new ScreenBroadcastReceiver().isScreenOn())) {
        //if (found==true)
//        ActivityManager am;
//        am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
//        List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1);
//        Log.d("topActivity", "CURRENT Activity ::"
//                + taskInfo.get(0).topActivity.getClassName());
//        ComponentName componentInfo = taskInfo.get(0).topActivity;
//        componentInfo.getPackageName();

       // KeyguardManager myKM = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
        Toast.makeText(getActivity(), "Warning: YOU MIGHT BE GETTING DISTRACTED!!!", Toast.LENGTH_LONG).show();
        //}

        getActivity().unregisterReceiver(receiver);
    }


    public String[] foobar(String s)
    {
        String arr[]=new String[2];
        if (s=="")
        {
            arr[0]="0";
            arr[1]="0";
        }
        else
        {
            StringTokenizer st=new StringTokenizer(s);
            arr[0]=st.nextToken();
            arr[1]=st.nextToken();
        }
        return arr;
    }

    //@SuppressLint("ValidFragment")
    //public class TestClass extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            final String[] hour = new String[1];
            final String[] minute = new String[1];
            final String[] second = {"00"};
            inflateView = inflater.inflate(R.layout.fragment_zen, container, false);


            btnStart = (Button) inflateView.findViewById(R.id.btnStart);
            btnStop = (Button) inflateView.findViewById(R.id.btnStop);
            textViewTime = (TextView) inflateView.findViewById(R.id.textViewTime);



            //final int hour = timePicker1.getCurrentHour();
            //final int minute = timePicker1.getCurrentMinute();

            //            if (s==null)
//            {
//                s="00:00:00";
//            }
            //System.out.println("The string picked up by the motherfucking cocksucking time picker : " +s);
//            StringTokenizer st=null;
//            if (s!=null) {
//                st = new StringTokenizer(s, ":");
//            }
//
////            System.out.println("Hours set by user: " + hour);
////            System.out.println("Minutes set by user: " + minute);
////            StringTokenizer st=new StringTokenizer("");
//            if (st!=null) {
//                hour = st.nextToken();
//                minute = st.nextToken();
//                second = st.nextToken();
//            }
            // + Integer.parseInt(second)*1000;


//            long param1 = hour * 60 * 60 + minute * 60;
//            final long param_in_millis = param1*1000;
//            System.out.println("Number of milliseconds calculated: "+param_in_millis);
            final CounterClass[] timer = new CounterClass[1];

            btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timePicker1 = (EditText) inflateView.findViewById(R.id.timePicker1);
                    String s = timePicker1.getText().toString();
                    int hour=0, minute=0, second=0;
                    StringTokenizer st = new StringTokenizer(s, ": ");
                    int l = st.countTokens();
                    if (l==0)
                    {
                        textViewTime.setText("The fields cannot be empty.");
                    }

                    if (l==1)
                    {
                        textViewTime.setText("Please enter numbers in the required format. Setting number of minutes to 0..");
                        hour = Integer.parseInt(st.nextToken());
                    }
                    if (l==2) {
                        hour = Integer.parseInt(st.nextToken());
                        minute = Integer.parseInt(st.nextToken());
                    }
                    if (l==3)
                    {
                        hour=Integer.parseInt(st.nextToken());
                        minute=Integer.parseInt(st.nextToken());
                        second=Integer.parseInt(st.nextToken());
                    }
                    System.out.println("Hour= "+hour);
                    System.out.println("Minute= "+minute);
//                    hour[0]=foobar(s)[0];
//                    System.out.println("Hour returned by function: " + hour[0]);
//                    minute[0] =foobar(s)[1];
//                    System.out.println("Minute returned by function: " + minute[0]);
                   // textViewTime.setText(hour[0] + ":" + minute[0] + ":" + second[0]);
                    String sent = hour + " : " + minute;
                    textViewTime.setText(sent);
                    final long total_milliseconds=(hour*60*60*1000) + (minute *60 *1000) + (second*1000);
                    System.out.println("Number of milliseconds = "+total_milliseconds);
//                    Calendar c = Calendar.getInstance();
//                    long now = c.getTimeInMillis();
//                    System.out.println("Current time: "+now);
//                    c.set(Calendar.HOUR_OF_DAY, hour);
//                    c.set(Calendar.MINUTE, minute);
//                    c.set(Calendar.SECOND, 0);
//                    c.set(Calendar.MILLISECOND, 0);
//                    System.out.println("Time set by user: " + hour + " hours" + minute + " minute" );
                   // long passed = now - c.getTimeInMillis();// Number of milliseconds passed so far in the day
                    //System.out.println(passed);
                    //long current_time = System.currentTimeMillis();
//                    Calendar rightNow = Calendar.getInstance();
//                    int hour = rightNow.get(Calendar.HOUR_OF_DAY);// Gets the hour of the day in 24h format
//                    int minute=rightNow.get(Calendar.MINUTE);// Gets the current minute
//
//                    long milliseconds_passed = hour*60*60*1000 + minute * 60 * 1000;
//                    System.out.println("Number of milli seconds passed so far: " +  milliseconds_passed);
//                    long difference=Math.abs(milliseconds_passed - param_in_millis);
                    //System.out.println("Current system time in milliseconds: " +current_time);

                 //   long time_difference_in_millis=Math.abs (current_time - param_in_millis);
                    //System.out.println("Time difference in millis: "+ difference);

                    timer[0] = new CounterClass(total_milliseconds, 1000);
                    timer[0].start();
                }
            });

            btnStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timer[0].cancel();
                }
            });
            return inflateView;
     //   }
    }


    public class CounterClass extends CountDownTimer
    {
        public CounterClass(long milli, long countDownInterval)
        {
            super(milli, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long millis = millisUntilFinished;
            String foo = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

            System.out.println(foo);
            textViewTime.setText(foo);


        }

        @Override
        public void onFinish() {
            textViewTime.setText("Countdown finished!!");

        }
    }


}


class ScreenBroadcastReceiver extends BroadcastReceiver
{
    boolean isScreenOn;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            Log.d("TAG", "The motherfucking screen is on.");
            isScreenOn = true;

        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            Log.d("TAG", "The buttfucking screen is off.");
            isScreenOn=false;
        }

        else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON))
        {
            KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            boolean locked = km.inKeyguardRestrictedInputMode();
            isScreenOn=locked;
        }
    }

    public boolean isScreenOn()
    {
        return isScreenOn;
    }
}