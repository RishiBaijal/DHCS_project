package in.ac.iiitd.dhcs.focus.MainTabs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Random;

import in.ac.iiitd.dhcs.focus.CustomUIClasses.AppDistributionView;
import in.ac.iiitd.dhcs.focus.CustomUIClasses.MeterView;
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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    MeterView productivityMeterView;
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflaterView=inflater.inflate(R.layout.fragment_productivity, container, false);
        productivityMeterView=(MeterView)inflaterView.findViewById(R.id.productivityMeterView);
        productivityMeterView.setProgress(20);
        productivityMeterView.setTarget(80);

        LinearLayout ll=(LinearLayout)inflaterView.findViewById(R.id.productivityLinearLayout);

        for(int i=0;i<10;i++) {
            Random random=new Random(9);

            AppDistributionView app1 = new AppDistributionView(getActivity(), null);
            ll.addView(app1);
            app1.setProgress(10*i);
            app1.setDuration((long) (0.5*i * 60 * 60 * 1000));
        }
        return inflaterView;
    }


}
