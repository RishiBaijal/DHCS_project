package in.ac.iiitd.dhcs.focus.MainTabs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.ac.iiitd.dhcs.focus.CustomUIClasses.TimerView;
import in.ac.iiitd.dhcs.focus.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ZenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ZenFragment extends Fragment {
    TimerView timerView;
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
        timerView= (TimerView) inflaterView.findViewById(R.id.timerView);
        timerView.setProgress(50);
        return inflaterView;
    }

    @Override
    public void onResume(){
        super.onResume();
//        final long totalTime=1*60*1000;//millisecond
//        new CountDownTimer(totalTime, 60) {
//            long minMillisLeft=totalTime;
//            public void onTick(long millisUntilFinished) {
////                Log.v(TAG,""+millisUntilFinished);
//                if(millisUntilFinished<minMillisLeft)
//                    minMillisLeft=millisUntilFinished;
//                timerView.setProgress(((float)(totalTime-minMillisLeft)/(float)totalTime)*100);
//                timerView.setProgressValue(totalTime-minMillisLeft);
//            }
//
//            public void onFinish() {
//
//            }
//        }
//           .start();
    }


}
