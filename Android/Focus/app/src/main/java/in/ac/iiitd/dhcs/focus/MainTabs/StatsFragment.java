package in.ac.iiitd.dhcs.focus.MainTabs;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.Date;

import in.ac.iiitd.dhcs.focus.Database.StatsFetcher;
import in.ac.iiitd.dhcs.focus.MainTabActivity;
import in.ac.iiitd.dhcs.focus.R;
import in.ac.iiitd.dhcs.focus.StatsObjects.ProductivePercentStatObject;
import in.ac.iiitd.dhcs.focus.StatsObjects.ProductiveTimeStatObject;
import in.ac.iiitd.dhcs.focus.StatsObjects.WeeklyPercentStatObject;
import in.ac.iiitd.dhcs.focus.StatsObjects.WeeklyTimeStatObject;

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

    private static final String TAG="StatsFragment";

    ArrayList<ProductiveTimeStatObject> statTimeList=new ArrayList<ProductiveTimeStatObject>();
    ArrayList<ProductivePercentStatObject> statPercentList=new ArrayList<ProductivePercentStatObject>();
    WeeklyTimeStatObject[] weekTimeList=new WeeklyTimeStatObject[8];
    WeeklyPercentStatObject[] weekPercentList=new WeeklyPercentStatObject[8];

    static Context context;
    Resources res;
    float density;
    
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
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the activity_main_tab for this fragment
        rootView = inflater.inflate(R.layout.fragment_stats, container, false);

        context=getActivity();
        res = context.getResources();
        density = res.getDisplayMetrics().density;
        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        statTimeList=StatsFetcher.getProductiveTime(getActivity(), "");
        statPercentList=StatsFetcher.getProductivePercent(getActivity(), "");
        weekTimeList=StatsFetcher.getWeeklyTime(getActivity(), "");
        weekPercentList=StatsFetcher.getWeeklyPercent(getActivity(),"");

        openTimeChart();
    }

    private void openTimeChart(){
        XYSeriesRenderer prodSeriesRenderer=setupTimeSeriesRenderer(getResources().getColor(R.color.primary)
                , getResources().getColor(R.color.productive_fill));
        XYSeriesRenderer useSeriesRenderer=setupTimeSeriesRenderer(getResources().getColor(R.color.accent)
                ,getResources().getColor(R.color.usage_fill));

        XYMultipleSeriesRenderer mRenderer=setupTimeMultipleRenderer("Days","Time (hrs)","Phone Usage (duration)");

        mRenderer.addSeriesRenderer(0,useSeriesRenderer);
        mRenderer.addSeriesRenderer(1,prodSeriesRenderer);

        TimeSeries prodTimeSeries=new TimeSeries("Productive");
        TimeSeries useTimeSeries=new TimeSeries("Total");

        for(ProductiveTimeStatObject ptso:statTimeList){
            double pDur=((((float)ptso.getProdDur()/1000f)/60f)/60f);
//            Log.v(TAG,pDur+" "+ptso.getDate());
            prodTimeSeries.add(new Date(ptso.getDate()), pDur);
//            prodTimeSeries.add(new Date(ptso.getDate()+(24*60*60*1000)), pDur);
            double uDur=((((float)ptso.getUseDur()/1000f)/60f)/60f);
//            Log.v(TAG,uDur+" "+ptso.getDate());
            useTimeSeries.add(new Date(ptso.getDate()),uDur);
//            useTimeSeries.add(new Date(ptso.getDate()+(24*60*60*1000)), uDur);
        }

        final XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(0,useTimeSeries);
        dataset.addSeries(1,prodTimeSeries);

        drawChart(useTimeSeries,dataset,mRenderer);
    }

    public XYSeriesRenderer setupTimeSeriesRenderer(int colour,int colourFill){
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setLineWidth((int)(2 * density));
        renderer.setColor(colour);

        renderer.setDisplayBoundingPoints(true);
        renderer.setDisplayChartValues(false);

        XYSeriesRenderer.FillOutsideLine fill = new XYSeriesRenderer.FillOutsideLine(
                XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ABOVE);
        fill.setColor(colourFill);
        renderer.addFillOutsideLine(fill);

        return renderer;
    }

    public XYMultipleSeriesRenderer setupTimeMultipleRenderer(String xTitle,String yTitle,String chartTitle){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float val = 15 * density;

        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

        mRenderer.setXLabels((int)(20 * density));
        mRenderer.setMarginsColor(Color.argb(0xff, 0xf0, 0xf0, 0xf0));

        mRenderer.setXLabelsAlign(Paint.Align.RIGHT);
        mRenderer.setXLabelsAngle(-45);

        mRenderer.setZoomEnabled(true);
        mRenderer.setPanEnabled(true,true);
        mRenderer.setClickEnabled(true);
        mRenderer.setInScroll(true);

        mRenderer.setZoomButtonsVisible(true);
        mRenderer.setYAxisMin(0);
        mRenderer.setChartTitleTextSize(val);
        mRenderer.setLabelsTextSize((float) (val*0.75));
        mRenderer.setLegendTextSize((float) (val*0.75));
        mRenderer.setAxisTitleTextSize((float) (val*0.75));
        mRenderer.setYTitle(yTitle);
        mRenderer.setXTitle("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n "+xTitle);
        mRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        mRenderer.setChartTitle(chartTitle);
        mRenderer.setShowGrid(true);
        int[] margins={(int)(20 * density),(int)(40 * density),(int)(25 * density),0};
        mRenderer.setMargins(margins);

        return mRenderer;
    }

    public void drawChart(TimeSeries mSeries,XYMultipleSeriesDataset dataset,XYMultipleSeriesRenderer mRenderer){
        int marginY=1;
        mRenderer.setYAxisMax(mSeries.getMaxY()+marginY);

        final GraphicalView chartView = ChartFactory.getTimeChartView(context, dataset, mRenderer, "Duration All-Time");

        chartView.setOnTouchListener(new View.OnTouchListener() {
            ViewPager mViewPager= MainTabActivity.mViewPager;
            @SuppressLint("WrongViewCast")
            ViewParent mParent= (ViewParent)rootView.findViewById(R.id.durationChart);

            float mFirstTouchX,mFirstTouchY;

            @Override
            public boolean onTouch(View arg0, MotionEvent event) {

                // save the position of the first touch so we can determine whether the user is dragging
                // left or right
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mFirstTouchX = event.getX();
                    mFirstTouchY = event.getY();
                }


                if (event.getPointerCount() > 1
                        || (event.getX() < mFirstTouchX)
                        || (event.getX() > mFirstTouchX)
                        || (event.getY() < mFirstTouchY)
                        || (event.getY() > mFirstTouchY)) {
                    mViewPager.requestDisallowInterceptTouchEvent(true);
                    mParent.requestDisallowInterceptTouchEvent(true);
                }
                else {
                    mViewPager.requestDisallowInterceptTouchEvent(false);
                    mParent.requestDisallowInterceptTouchEvent(true);
                }
                // TODO Auto-generated method stub
                return false;
            }

        });

        LinearLayout chart_container=(LinearLayout)rootView.findViewById(R.id.durationChart);
        chart_container.addView(chartView,0);
    }

}