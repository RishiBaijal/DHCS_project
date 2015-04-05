package in.ac.iiitd.dhcs.focus.MainTabs;


import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
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
    }

    private void setButtons(View inflaterView) {
        startBtn=(Button) inflaterView.findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainTabActivity.zenStarted = true;
                long setMillis=(((timePicker.getCurrentHour()*60+timePicker.getCurrentMinute())*60)*1000);
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
                    zenTimer.cancel();
                    viewFlipper.showNext();
                    timePicker.setCurrentHour(0);
                    timePicker.setCurrentMinute(0);
                }

            }
        });
    }

}
