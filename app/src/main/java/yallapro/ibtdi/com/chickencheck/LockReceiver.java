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
    Context context;
    Runnable run;
    SharedPreferences pref;
    String tempr,humadity,created_at;

    @Override
    public void onReceive(final Context context, Intent intent) {
    //    mData(context);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
              ArrayList<Mydata> data1 = getData(context);
                checkAndFilter(data1);

            }
        },TimeUnit.SECONDS.toMillis(1), TimeUnit.MINUTES.toMillis(1));

       // runThread();
    }



    private void checkAndFilter(ArrayList<Mydata> data1) {
        String date;
        String hum;
        String temp;
        if (!data1.isEmpty()){
            for (Mydata element:data1) {
                int tid = element.tid;
                date = element.date;
                hum = element.hum;
                 temp = element.temp;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                try {


                    String s = DateUtils.getRelativeTimeSpanString(sdf.parse(date).getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();

                    if(s.matches("[54321]{1} hour. ago")){
                            weath.put(s,new MyWeather(temp,hum));
                    }
                    else if (s.contains("minute")){
                        weath.put(s,new MyWeather(temp,hum));

                    }
                    else {
                        System.out.println(s);

                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private ArrayList<Mydata> getData(final Context context) {
        StringRequest request = new StringRequest(Request.Method.GET, link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray feedsArray = obj.getJSONArray("feeds");
                    for (int i = 0; i <feedsArray.length() ; i++) {
                        JSONObject jsonObject = feedsArray.getJSONObject(i);
                        int entry_id = jsonObject.getInt("entry_id");
                        tempr = jsonObject.getString("field1");
                        humadity = jsonObject.getString("field2");
                        created_at = jsonObject.getString("created_at");
                        Timer timer = new Timer();
                        timer.scheduleAtFixedRate(new TimerTask() {
                            @Override
                            public void run() {
                                pref=context.getSharedPreferences("Data",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor=pref.edit();
                                editor.putString("tempreature",tempr).putString("humadity",humadity).putString("createAt",created_at).apply();
                                Log.e("temp and humadity",tempr+"...."+humadity+"....."+created_at);
                            }
                        },TimeUnit.SECONDS.toMillis(1), TimeUnit.MINUTES.toMillis(1));
                    data.add(new Mydata(entry_id,tempr,humadity,created_at));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

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

    private void runThread(){
        Handler handler = new Handler();
        run=new Runnable() {
            @Override
            public void run() {

            }
        };

        handler.postDelayed(run, 3000);

    }

    private void mData(Context context) {

        Intent newIntent = new Intent(context, MainActivity.class);
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
                .setContentText("Warning Check Your Safety Rule ")
                .setVibrate(new long[]{1000, 2000, 1000, 2000, 1000, 2000})
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
