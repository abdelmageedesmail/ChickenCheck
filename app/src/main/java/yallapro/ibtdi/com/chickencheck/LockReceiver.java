package yallapro.ibtdi.com.chickencheck;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class LockReceiver extends BroadcastReceiver {
    ArrayList<Mydata> data = new ArrayList<Mydata>();
    HashMap<String,MyWeather> weath = new HashMap<>();
    String link="http://api.thingspeak.com/channels/204531/feeds.json?api_key=94G47FDM6PCUFTU8";
    //Context context;
    Runnable run;
    SharedPreferences pref;
    String tempr,humadity,created_at;
    private int result=0;
    private float tmp=0;
    private int last_Id=0;
    private int entry_id=0;
    private String s;
    private ArrayList<String> tails;

    @Override
    public void onReceive(final Context context, Intent intent) {
    //    mData(context);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
             getData(context);
              // checkAndFilter(data1,context);

            }
        },TimeUnit.SECONDS.toMillis(20), TimeUnit.MINUTES.toMillis(30));

       // runThread();
    }
    private ArrayList<Mydata> getData(final Context context) {

        StringRequest request = new StringRequest(Request.Method.GET, link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id=0;
                try {

                    JSONObject obj = new JSONObject(response);
                    JSONArray feedsArray = obj.getJSONArray("feeds");
                    if(last_Id>0){
                    JSONObject jsonObject1 = feedsArray.getJSONObject(last_Id-1);
                     id = jsonObject1.getInt("entry_id");
                        Log.e("id",id+"");
                    }
                    if(last_Id!=id || id==0){
                    for (int i = last_Id; i <feedsArray.length() ; i++) {
                        JSONObject jsonObject = feedsArray.getJSONObject(i);
                         entry_id = jsonObject.getInt("entry_id");
                        tempr = jsonObject.getString("field1");
                        humadity = jsonObject.getString("field2");
                        created_at = jsonObject.getString("created_at");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                         s = DateUtils.getRelativeTimeSpanString(sdf.parse(created_at).getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
                        data.add(new Mydata(entry_id,tempr,humadity,s));
                    }}
                    last_Id=entry_id;
                    Log.e("last_id:",last_Id+"");


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                for (Mydata element:data
                     ) {
                    if (!element.temp.isEmpty()){
                         tmp = Float.parseFloat(element.temp);
                        Log.e("tmp= ", ""+tmp);
                        result+=tmp;
                    }
                }
                if (data.size()>0){
              float avg= result/data.size();
                Log.e("res= ", ""+result);
                Log.e("avg= ", ""+avg);

                //Log.e("condition="+data.get(data.size()-1).date+" "+data.get(data.size()-2).date," "+data.get(data.size()-1).date.equalsIgnoreCase(data.get(data.size()-2).date));
                result=0;
                if (avg>=47 || avg<=18 && (last_Id!=id || id==0)){
                    mData(context,avg);


                }}

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error+"", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(context).add(request);

        return data;
    }



    private void mData(Context context,float avg) {

        Intent newIntent = new Intent(context, ChartActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, newIntent,
                                        PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker("Notification")
                .setLargeIcon(bitmap)
                .setSmallIcon(R.mipmap.logo)
                .setContentIntent(pendingIntent)
                .setContentTitle("Notification")
                .setContentText("Warning tempreture is  "+avg)
                .setSound(defaultSoundUri)
                .setAutoCancel(true);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, builder.build());
    }

    public class MyWeather {
        private String tempr,humd;

        public MyWeather(String temp, String humd) {
            this.tempr = temp;
            this.humd = humd;
        }

        public MyWeather() {
        }

        public String getTempr() {
            return tempr;
        }

        public void setTempr(String tempr) {
            this.tempr = tempr;
        }

        public String getHumd() {
            return humd;
        }

        public void setHumd(String humd) {
            this.humd = humd;
        }
    }
}
