package in.ac.iiitd.dhcs.focus;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.Date;

import in.ac.iiitd.dhcs.focus.Database.StatsFetcher;
import in.ac.iiitd.dhcs.focus.StatsObjects.ProductivePercentStatObject;
import in.ac.iiitd.dhcs.focus.StatsObjects.ProductiveTimeStatObject;
import in.ac.iiitd.dhcs.focus.StatsObjects.WeeklyPercentStatObject;
import in.ac.iiitd.dhcs.focus.StatsObjects.WeeklyTimeStatObject;


public class AppStatsActivity extends ActionBarActivity {

    ArrayList<ProductiveTimeStatObject> statTimeList=new ArrayList<ProductiveTimeStatObject>();
    ArrayList<ProductivePercentStatObject> statPercentList=new ArrayList<ProductivePercentStatObject>();
    WeeklyTimeStatObject[] weekTimeList=new WeeklyTimeStatObject[8];
    WeeklyPercentStatObject[] weekPercentList=new WeeklyPercentStatObject[8];

    Resources res;
    float density;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_stats);
        res = getResources();
        density = res.getDisplayMetrics().density;
    }

    @Override
    public void onResume(){
        super.onResume();

        Intent intent = getIntent();
        String appName=intent.getStringExtra("appName");

        setTitle(appName+" "+"Statistics");

        statTimeList= StatsFetcher.getProductiveTime(this, appName);
//        statPercentList=StatsFetcher.getProductivePercent(this, appName);
        weekTimeList=StatsFetcher.getWeeklyTime(this, appName);
//        weekPercentList=StatsFetcher.getWeeklyPercent(this,appName);

        openDurationChart();
        openWeeklyDurationChart();
//        openPercentChart();
//        openWeeklyPercentChart();
    }

    private void openWeeklyDurationChart(){
        XYSeriesRenderer prodSeriesRenderer=setupSeriesRenderer(getResources().getColor(R.color.primary)
                , getResources().getColor(R.color.productive_fill));
        XYSeriesRenderer useSeriesRenderer=setupSeriesRenderer(getResources().getColor(R.color.accent)
                ,getResources().getColor(R.color.usage_fill));

        XYMultipleSeriesRenderer mRenderer=setupMultipleRenderer("Days","Time (hrs)","Day-of-Week Usage (duration)");

        mRenderer.setBarSpacing((int)(2 * density));
        mRenderer.setXLabels(0);

        mRenderer.addSeriesRenderer(0,useSeriesRenderer);
        mRenderer.addSeriesRenderer(1,prodSeriesRenderer);


        XYSeries prodBarSeries=new XYSeries("Productive");
        XYSeries useBarSeries=new XYSeries("Total");

        for(int i=1;i<weekTimeList.length;i++){
            WeeklyTimeStatObject wtso=weekTimeList[i];
            double pDur=((((float)wtso.getProdDur()/1000f)/60f)/60f);
            prodBarSeries.add(i, pDur);
            double uDur=((((float)wtso.getUseDur()/1000f)/60f)/60f);
            useBarSeries.add(i,uDur);
        }

        for(int i=1; i< weekTimeList.length;i++){
            WeeklyTimeStatObject wtso=weekTimeList[i];
            mRenderer.addXTextLabel(i, wtso.getday());
        }

        final XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(0,useBarSeries);
        dataset.addSeries(1,prodBarSeries);


        int marginY=1;
        mRenderer.setYAxisMax(useBarSeries.getMaxY()+marginY);
        mRenderer.setYAxisMin(0);
        mRenderer.setXAxisMax(8);
        mRenderer.setXAxisMin(0);

        mRenderer.setPanLimits(new double[] {0,8,
                (double)0,useBarSeries.getMaxY()+marginY});

        drawBarChart(dataset, mRenderer, R.id.durationBarChart);
    }

    private void openWeeklyPercentChart(){
        XYSeriesRenderer prodSeriesRenderer=setupSeriesRenderer(getResources().getColor(R.color.primary)
                , getResources().getColor(R.color.productive_fill));

        XYMultipleSeriesRenderer mRenderer=setupMultipleRenderer("Days","Percentage (%)","Day-of-Week Usage (percentage)");

        mRenderer.setBarSpacing((int)(2 * density));
        mRenderer.setXLabels(0);

        mRenderer.addSeriesRenderer(0,prodSeriesRenderer);
        int marginY=1;
        mRenderer.setYAxisMax(100);
        mRenderer.setYAxisMin(0);

        XYSeries prodBarSeries=new XYSeries("Productive");

        for(int i=1;i<weekPercentList.length;i++){
            WeeklyPercentStatObject wpso=weekPercentList[i];
            double pPer=wpso.getProdPercent();
            prodBarSeries.add(i, pPer);
        }

        for(int i=1; i< weekTimeList.length;i++){
            WeeklyTimeStatObject wtso=weekTimeList[i];
            mRenderer.addXTextLabel(i, wtso.getday());
        }

        final XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(0,prodBarSeries);

        mRenderer.setXAxisMax(8);
        mRenderer.setXAxisMin(0);

        mRenderer.setPanLimits(new double[] {0,8,
                (double)0,100});

        drawBarChart(dataset, mRenderer, R.id.percentBarChart);
    }

    private void openDurationChart(){
        XYSeriesRenderer prodSeriesRenderer=setupSeriesRenderer(getResources().getColor(R.color.primary)
                , getResources().getColor(R.color.productive_fill));
        XYSeriesRenderer useSeriesRenderer=setupSeriesRenderer(getResources().getColor(R.color.accent)
                , getResources().getColor(R.color.usage_fill));

        XYMultipleSeriesRenderer mRenderer=setupMultipleRenderer("Days", "Time (hrs)", "All-Time Usage (duration)");

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

        int marginY=1;
        mRenderer.setYAxisMax(useTimeSeries.getMaxY()+marginY);
        mRenderer.setYAxisMin(0);
        mRenderer.setXAxisMax(prodTimeSeries.getMaxX());
        mRenderer.setXAxisMin(prodTimeSeries.getMinX());


        mRenderer.setPanLimits(new double[] {useTimeSeries.getMinX(),useTimeSeries.getMaxX(),
                (double)0,useTimeSeries.getMaxY()+marginY});


        drawTimeChart(dataset, mRenderer, R.id.durationChart);
    }

    private void openPercentChart(){
        XYSeriesRenderer prodSeriesRenderer=setupSeriesRenderer(getResources().getColor(R.color.primary)
                , getResources().getColor(R.color.productive_fill));

        XYMultipleSeriesRenderer mRenderer=setupMultipleRenderer("Days", "Percentage (%)", "All-Time Usage (percentage)");

        mRenderer.addSeriesRenderer(0,prodSeriesRenderer);

        mRenderer.setYAxisMax(100);
        mRenderer.setYAxisMin(0);

        TimeSeries prodTimeSeries=new TimeSeries("Productive");

        for(ProductivePercentStatObject ppso:statPercentList){
            double pPer=ppso.getProdPercent();
//            Log.v(TAG,pDur+" "+ptso.getDate());
            prodTimeSeries.add(new Date(ppso.getDate()), pPer);
//            prodTimeSeries.add(new Date(ppso.getDate()+(24*60*60*1000)), pPer);
        }

        final XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(0,prodTimeSeries);

        mRenderer.setXAxisMax(prodTimeSeries.getMaxX());
        mRenderer.setXAxisMin(prodTimeSeries.getMinX());

        mRenderer.setPanLimits(new double[] {prodTimeSeries.getMinX(),prodTimeSeries.getMaxX(),
                (double)0,100});

        drawTimeChart(dataset, mRenderer, R.id.percentChart);
    }

    public XYSeriesRenderer setupSeriesRenderer(int colour,int colourFill){
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

    public XYMultipleSeriesRenderer setupMultipleRenderer(String xTitle,String yTitle,String chartTitle){
        float val = 15 * density;

        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

        mRenderer.setXLabels((int)(20 * density));
        mRenderer.setMarginsColor(Color.argb(0xff, 0xf0, 0xf0, 0xf0));

        mRenderer.setXLabelsAlign(Paint.Align.RIGHT);
        mRenderer.setXLabelsAngle(-45);

        mRenderer.setZoomEnabled(false,false);
        mRenderer.setPanEnabled(false,false);
//        mRenderer.setClickEnabled(false);
//        mRenderer.setInScroll(false);

        mRenderer.setZoomButtonsVisible(false);
        mRenderer.setYAxisMin(0);
        mRenderer.setChartTitleTextSize(val);
        mRenderer.setLabelsTextSize((float) (val*0.75));
        mRenderer.setLegendTextSize((float) (val*0.75));
        mRenderer.setAxisTitleTextSize((float) (val*0.75));
        mRenderer.setLabelsColor(getResources().getColor(R.color.graph_text));
        mRenderer.setYLabelsColor(0, getResources().getColor(R.color.graph_text));
        mRenderer.setXLabelsColor(getResources().getColor(R.color.graph_text));
        mRenderer.setYTitle(yTitle);
        mRenderer.setXTitle("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n "+xTitle);
        mRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        mRenderer.setChartTitle(chartTitle);
        mRenderer.setShowGrid(true);
        int[] margins={(int)(40 * density),(int)(40 * density),(int)(60 * density),(int)(10 * density)};
        mRenderer.setMargins(margins);

        return mRenderer;
    }

    public void drawTimeChart(XYMultipleSeriesDataset dataset,XYMultipleSeriesRenderer mRenderer,int id){

        final GraphicalView chartView = ChartFactory.getTimeChartView(this, dataset, mRenderer, "Duration All-Time");

//        chartView.setOnTouchListener(new View.OnTouchListener() {
//            ViewPager mViewPager= MainTabActivity.mViewPager;
//            @SuppressLint("WrongViewCast")
//            ViewParent mParent= (ViewParent)findViewById(R.id.durationChart);
//
//            float mFirstTouchX,mFirstTouchY;
//
//            @Override
//            public boolean onTouch(View arg0, MotionEvent event) {
//
//                // save the position of the first touch so we can determine whether the user is dragging
//                // left or right
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    mFirstTouchX = event.getX();
//                    mFirstTouchY = event.getY();
//                }
//
//
//                if (event.getPointerCount() > 1
//                        || (event.getX() < mFirstTouchX)
//                        || (event.getX() > mFirstTouchX)
//                        || (event.getY() < mFirstTouchY)
//                        || (event.getY() > mFirstTouchY)) {
//                    mViewPager.requestDisallowInterceptTouchEvent(true);
//                    mParent.requestDisallowInterceptTouchEvent(true);
//                }
//                else {
//                    mViewPager.requestDisallowInterceptTouchEvent(false);
//                    mParent.requestDisallowInterceptTouchEvent(true);
//                }
//                // TODO Auto-generated method stub
//                return false;
//            }
//
//        });

        LinearLayout chart_container=(LinearLayout)findViewById(id);
        chart_container.addView(chartView,0);
    }

    public void drawBarChart(XYMultipleSeriesDataset dataset,XYMultipleSeriesRenderer mRenderer,int id){
        final GraphicalView chartView = ChartFactory.getBarChartView(this, dataset, mRenderer, BarChart.Type.DEFAULT);
//        chartView.setOnTouchListener(new View.OnTouchListener() {
//            ViewPager mViewPager= MainTabActivity.mViewPager;
//            @SuppressLint("WrongViewCast")
//            ViewParent mParent= (ViewParent)findViewById(R.id.durationChart);
//
//            float mFirstTouchX,mFirstTouchY;
//
//            @Override
//            public boolean onTouch(View arg0, MotionEvent event) {
//
//                // save the position of the first touch so we can determine whether the user is dragging
//                // left or right
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    mFirstTouchX = event.getX();
//                    mFirstTouchY = event.getY();
//                }
//
//                if (event.getPointerCount() > 1
//                        || (event.getX() < mFirstTouchX)
//                        || (event.getX() > mFirstTouchX)
//                        || (event.getY() < mFirstTouchY)
//                        || (event.getY() > mFirstTouchY)) {
//                    mViewPager.requestDisallowInterceptTouchEvent(true);
//                    mParent.requestDisallowInterceptTouchEvent(true);
//                }
//                else {
//                    mViewPager.requestDisallowInterceptTouchEvent(false);
//                    mParent.requestDisallowInterceptTouchEvent(true);
//                }
//                // TODO Auto-generated method stub
//                return false;
//            }
//
//        });
        LinearLayout chart_container=(LinearLayout)findViewById(id);
        chart_container.addView(chartView,0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_app_stats, menu);
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
}
