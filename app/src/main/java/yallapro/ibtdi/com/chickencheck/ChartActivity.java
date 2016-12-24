package yallapro.ibtdi.com.chickencheck;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


//import com.jjoe64.graphview.*;

//import lecho.lib.hellocharts.view.LineChartView;

public class ChartActivity extends AppCompatActivity {

    private BarChart barChart;
    ArrayList<BarEntry> entriesGroup1;
    ArrayList<BarEntry> entriesGroup2;
    BarDataSet barDataSet1;
    SharedPreferences pref;
    String temp,humadity,createAt;
    ArrayList<Mydata> data = new ArrayList<Mydata>();
    private String url="http://api.thingspeak.com/channels/204531/feeds.json?api_key=94G47FDM6PCUFTU8";
    private BarDataSet barDataSet2;
    ArrayList<String> labels;
    ArrayList<IBarDataSet> dataSets;
    private BarData barData;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");;
    private float[] ax={30,22,40,61,82,10};
    private float[] ax2={14,32,54,17,94,11};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        //LineChartView chartView = (LineChartView) findViewById(R.id.chart);

        barChart = (BarChart) findViewById(R.id.chart);

        startService(new Intent(this,GetUserRegisterServices.class));


         entriesGroup1 = new ArrayList<>();
         entriesGroup2 = new ArrayList<>();
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
         barDataSet1 = new BarDataSet(entriesGroup1, "temperature");
         barDataSet2 = new BarDataSet(entriesGroup2, "Humidity");
        barDataSet1.setColor(Color.parseColor("#ff3d00"));
        barDataSet1.setBarSpacePercent(1f);
        barDataSet2.setColor(Color.parseColor("#ffea00"));
        barDataSet2.setBarSpacePercent(1f);
         labels = new ArrayList<String>();
        labels.add("now");
        labels.add("1Hr");
        labels.add("2Hr");
        labels.add("3Hr");
        labels.add("4Hr");
        labels.add("5Hr");
         dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);

         barData = new BarData(labels, dataSets);
        barChart.animateXY(2000, 2000);
        barChart.setData(barData);
        barChart.setContentDescription("");
        barChart.setDescription("");
        barChart.getAxisRight().setEnabled(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setTextColor(Color.RED);
        barChart.notifyDataSetChanged();
        barChart.invalidate();
        barChart.setTouchEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getTempData("http://api.thingspeak.com/channels/204531/feeds.json?api_key=94G47FDM6PCUFTU8");


            }
        },TimeUnit.SECONDS.toMillis(10),TimeUnit.MINUTES.toMillis(1));

    }


    private ArrayList<Mydata> getTempData(String url) {
        entriesGroup1.clear();
        entriesGroup2.clear();
        labels.clear();
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray feedsArray = obj.getJSONArray("feeds");
                    for (int i = 0; i < feedsArray.length()-9; i++) {
                        JSONObject jsonObject = feedsArray.getJSONObject(i);
                        int entry_id = jsonObject.getInt("entry_id");
                        final String tempr = jsonObject.getString("field1");
                        final String humadity = jsonObject.getString("field2");
                        String created_at = jsonObject.getString("created_at");
                        final String s = DateUtils.getRelativeTimeSpanString(sdf.parse(created_at).getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
                        boolean matches = s.matches("\\d+ (minute|hour|day|month). ago");
                        System.out.println(s);
                        System.out.println(matches+"");
                        if(s.matches("\\d+ (minute|hour|day|month). ago")|| s.equalsIgnoreCase("yesterday")){

                            final int finalI = i;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println(finalI);
                                    barChart.removeAllViews();
                                   ax=new float[6];
                                    ax[finalI]=Float.parseFloat(tempr);
                                    ax2=new float[6];
                                    ax2[finalI]=Float.parseFloat(humadity);
                                    entriesGroup1.add(new BarEntry(ax, finalI));
                                    entriesGroup2.add(new BarEntry(ax2, finalI));
                                    labels.add(s);


                                    barChart.notifyDataSetChanged();
                                    barChart.invalidate();
                                }
                            });

                            System.out.println(s+"  "+"done");



                    }
                }} catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChartActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(ChartActivity.this).add(request);
        //Log.e("list",data.get(0).date);
    return data;
    }

    private void recieveThread(){

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pref=getSharedPreferences("Data",MODE_PRIVATE);
                        temp=pref.getString("tempreature","null");
                        humadity=pref.getString("humadity","null");
                        createAt=pref.getString("createAt","null");

                        Log.e("Data",temp+"....."+humadity+"......."+createAt);
                    }
                });

            }
        }, TimeUnit.SECONDS.toMillis(1), TimeUnit.MINUTES.toMillis(1));

    }

}
