package yallapro.ibtdi.com.chickencheck;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
//import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BaseDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
//import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

//import com.jjoe64.graphview.*;

//import lecho.lib.hellocharts.view.LineChartView;

public class ChartActivity extends AppCompatActivity {

    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        //LineChartView chartView = (LineChartView) findViewById(R.id.chart);

        barChart = (BarChart) findViewById(R.id.chart);

        startService(new Intent(this,GetUserRegisterServices.class));
        float[] ax={30,22,40,61,82,10};
        float[] ax2={14,32,54,17,94,11};
        ArrayList<BarEntry> entriesGroup1 = new ArrayList<>();
        ArrayList<BarEntry> entriesGroup2 = new ArrayList<>();
        entriesGroup1.add(new BarEntry(ax[0],0));//x,y(temp)
        entriesGroup1.add(new BarEntry(ax[1],1));
        entriesGroup1.add(new BarEntry(ax[2],2));
        entriesGroup1.add(new BarEntry(ax[3],3));
        entriesGroup1.add(new BarEntry(ax[4],4));
        entriesGroup1.add(new BarEntry(ax[5],5));
        entriesGroup2.add(new BarEntry(ax2[0],0));
        entriesGroup2.add(new BarEntry(ax2[1],1));
        entriesGroup2.add(new BarEntry(ax2[2],2));
        entriesGroup2.add(new BarEntry(ax2[3],3));
        entriesGroup2.add(new BarEntry(ax2[4],4));
        entriesGroup2.add(new BarEntry(ax2[5],5));
        BarDataSet barDataSet1 = new BarDataSet(entriesGroup1, "temperature");
        BarDataSet barDataSet2 = new BarDataSet(entriesGroup2, "Humidity");
        barDataSet1.setColor(Color.CYAN);
        barDataSet1.setBarSpacePercent(1f);
        barDataSet2.setColor(Color.parseColor("#F57C00"));
        barDataSet2.setBarSpacePercent(1f);
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("now");
        labels.add("1Hr");
        labels.add("2Hr");
        labels.add("3Hr");
        labels.add("4Hr");
        labels.add("5Hr");
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        BarData barData = new BarData(labels, dataSets);

        barChart.setData(barData);
        barChart.setContentDescription("");
        barChart.setDescription("");
        barChart.getAxisRight().setEnabled(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setTextColor(Color.RED);
        barChart.invalidate();
       /* Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);
        barChart.groupBars(0f,0.0f,0.00f);//start,between space, external space
        XAxis xAxis = barChart.getXAxis();
        xAxis.setAxisMaximum(8);
        xAxis.setAxisMinimum(0);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new MyAxisValueFormatter());
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.RED);
       barChart.getAxisRight().setEnabled(false);
        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMinimum(0);
        yAxis.setAxisMaximum(100);
        barChart.invalidate();

*/


    }
}
