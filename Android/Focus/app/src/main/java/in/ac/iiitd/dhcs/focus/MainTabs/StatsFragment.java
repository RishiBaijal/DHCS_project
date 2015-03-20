package in.ac.iiitd.dhcs.focus.MainTabs;


import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.ac.iiitd.dhcs.focus.R;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import org.achartengine.model.CategorySeries;

import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;


import android.content.Intent;
import android.graphics.Color;

import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String[] mDays = new String[]{
            "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat",
            "Sun"  };


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatsFragment newInstance(String param1, String param2) {
        StatsFragment fragment = new StatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public StatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            Intent intent = getActivity().getIntent();
        }

    }

    LinearLayout mLayout;
    View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_stats, container, false);
        //openPieChart();
        //openScatterChart();
        openTimeChart();
        return mView;
    }

    private void openTimeChart(){

        int count = 5;
        Date[] dt = new Date[5];
        for(int i=0;i<count;i++){
            GregorianCalendar gc = new GregorianCalendar(2015, 2, i+1);
            dt[i] = gc.getTime();
        }

        int[] prodVals = { 2000,2500,2700,2100,2800};

        TimeSeries prodSeries = new TimeSeries("PRODUCTIVITY");

        for(int i=0;i<dt.length;i++){
            prodSeries.add(dt[i], prodVals[i]);

        }

        // Creating a dataset to hold each series
        final XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();


        dataset.addSeries(prodSeries);


        // Creating XYSeriesRenderer to customize visitsSeries
        XYSeriesRenderer prodRenderer = new XYSeriesRenderer();
        prodRenderer.setColor(Color.MAGENTA);
        prodRenderer.setPointStyle(PointStyle.CIRCLE);
        prodRenderer.setPointStrokeWidth(5);
        prodRenderer.setFillPoints(true);
        prodRenderer.setLineWidth(2f);
        prodRenderer.setDisplayChartValues(true);
        prodRenderer.setChartValuesTextSize(25);
        prodRenderer.setColor(getResources().getColor(R.color.accent));
        //prodRenderer.setShowLegendItem(false);


        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();

       // multiRenderer.setChartTitle("PRODUCTIVITY VS TIME Chart");
        multiRenderer.setXTitle("Days");
        multiRenderer.setYTitle("Time");
        multiRenderer.setXAxisColor(Color.BLACK);
        multiRenderer.setYAxisColor(Color.BLACK);
        multiRenderer.setAxisTitleTextSize(40);
        multiRenderer.setXLabels(20);
        multiRenderer.setYLabels(20);
        multiRenderer.setYAxisAlign(Paint.Align.LEFT,0);
        multiRenderer.setZoomButtonsVisible(true);
        multiRenderer.setMarginsColor(Color.argb(0xff, 0xf0, 0xf0, 0xf0));


        multiRenderer.addSeriesRenderer(prodRenderer);

        LinearLayout chartContainer = (LinearLayout) mView.findViewById(R.id.dashboard_time_layout2);

        // Creating a Time Chart
        GraphicalView ChartView;// = (GraphicalView) ChartFactory.getView(getActivity().getApplicationContext(), dataset, multiRenderer);
        ChartView = (GraphicalView) ChartFactory.getTimeChartView(getActivity().getApplicationContext(), dataset, multiRenderer,"dd-MMM-yyyy");
        chartContainer.addView(ChartView);





    }








}