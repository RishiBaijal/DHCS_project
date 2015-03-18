package in.ac.iiitd.dhcs.focus.MainTabs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.ac.iiitd.dhcs.focus.R;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;

import org.achartengine.chart.PointStyle;
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
        openPieChart();
        openScatterChart();
        return mView;
    }

    private void openPieChart() {
        String[] appNamesArray = new String[] {
                "Whatsapp","Facebook","Abc","PQR","XYZ","TWITTER"
        };

        // Pie Chart Section Value
        double[] usageArray = { 3.9, 12.9, 55.8, 1.9, 23.7, 1.8 } ;

        // Color of each Pie Chart Sections
        int[] colors = { Color.BLUE, Color.MAGENTA, Color.GREEN, Color.CYAN, Color.RED,
                Color.YELLOW };

        // Instantiating CategorySeries to plot Pie Chart
        CategorySeries distributionSeries = new CategorySeries("APP USAGE FOR TODAY! ");
        for(int i=0 ;i < usageArray.length;i++){
            // Adding a slice with its values and name to the Pie Chart
            distributionSeries.add(appNamesArray[i], usageArray[i]);
        }

        // Instantiating a renderer for the Pie Chart
        DefaultRenderer defaultRenderer  = new DefaultRenderer();
        for(int i = 0 ;i<usageArray.length;i++) {
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            seriesRenderer.setColor(colors[i]);
            seriesRenderer.setDisplayBoundingPoints(true);
            // Adding a renderer for a slice
            defaultRenderer.addSeriesRenderer(seriesRenderer);
        }
        defaultRenderer.setChartTitle("Today's usage Statistics ");
        defaultRenderer.setChartTitleTextSize(60);
        defaultRenderer.setShowLabels(true);
        defaultRenderer.setLabelsTextSize(40);
        defaultRenderer.setLabelsColor(Color.BLACK);
        defaultRenderer.setZoomButtonsVisible(true);


        GraphicalView chartView;
		/*if (chartView != null) {
			chartView.repaint();
		}
		else { */
        chartView = ChartFactory.getPieChartView(getActivity().getApplicationContext(),distributionSeries,defaultRenderer);
        LinearLayout layout = (LinearLayout) mView.findViewById(R.id.dashboard_chart_layout);
        //layout.removeAllViews();
        layout.addView(chartView);

    }

    private void openScatterChart(){
        String[] titles = new String[] { "XYZ", "ABC", "FAECBOOK", "GMAIL", "WHATSAPP" };
        List<double[]> x = new ArrayList<>();
        List<double[]> values = new ArrayList<double[]>();
        int count = 20;
        int length = titles.length;
        Random r = new Random();
        for (int i = 0; i < length; i++) {
            double[] xValues = new double[count];
            double[] yValues = new double[count];
            for (int k = 0; k < count; k++) {
                xValues[k] = k + r.nextInt() % 10;
                yValues[k] = k * 2 + r.nextInt() % 10;
            }
            x.add(xValues);
            values.add(yValues);
        }
        int[] colors = new int[] { Color.BLUE, Color.CYAN, Color.MAGENTA, Color.LTGRAY, Color.GREEN };
        PointStyle[] styles = new PointStyle[] { PointStyle.X, PointStyle.DIAMOND, PointStyle.TRIANGLE,
                PointStyle.SQUARE, PointStyle.CIRCLE };
        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
        setChartSettings(renderer, "TODAY'S USAGE", "X", "Y", -10, 30, -10, 51, Color.GRAY,
                Color.LTGRAY);
        renderer.setXLabels(10);
        renderer.setYLabels(10);
        length = renderer.getSeriesRendererCount();
        for (int i = 0; i < length; i++) {
            ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
        }
        // return ChartFactory.getScatterChartIntent(getActivity().getApplicationContext(), buildDataset(titles, x, values), renderer);
        // Getting a reference to LinearLayout of the MainActivity Layout
        LinearLayout chartContainer = (LinearLayout) mView.findViewById(R.id.dashboard_time_layout2);

        // Creating a Time Chart
        GraphicalView ChartView = (GraphicalView) ChartFactory.getScatterChartView(getActivity().getApplicationContext(), buildDataset(titles, x, values), renderer);

        chartContainer.addView(ChartView);


    }

    protected XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        setRenderer(renderer, colors, styles);
        return renderer;
    }

    protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle[] styles) {
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        renderer.setPointSize(5f);
        renderer.setMargins(new int[] { 20, 30, 15, 20 });
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(colors[i]);
            r.setPointStyle(styles[i]);
            renderer.addSeriesRenderer(r);
        }
    }
    protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
                                    String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,
                                    int labelsColor) {
        renderer.setChartTitle(title);
        renderer.setXTitle(xTitle);
        renderer.setYTitle(yTitle);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
    }
    protected XYMultipleSeriesDataset buildDataset(String[] titles, List<double[]> xValues,
                                                   List<double[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        addXYSeries(dataset, titles, xValues, yValues, 0);
        return dataset;
    }
    public void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles, List<double[]> xValues,
                            List<double[]> yValues, int scale) {
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            XYSeries series = new XYSeries(titles[i], scale);
            double[] xV = xValues.get(i);
            double[] yV = yValues.get(i);
            int seriesLength = xV.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(xV[k], yV[k]);
            }
            dataset.addSeries(series);
        }
    }



}