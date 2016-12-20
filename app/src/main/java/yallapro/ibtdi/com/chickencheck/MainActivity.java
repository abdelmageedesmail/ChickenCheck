package yallapro.ibtdi.com.chickencheck;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {


    ProgressBar progressbarOne,progressBarSec;
    TextView precent;
    Handler progressHandler = new Handler();
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        definedData();
        firstThread();

        secThread();

//        startService(new Intent(MainActivity.this,GetUserRegisterServices.class));
    }


    private void definedData(){
        progressbarOne=(ProgressBar) findViewById(R.id.progressBarFloat);
        progressBarSec=(ProgressBar) findViewById(R.id.progressBarSecond);
        precent=(TextView) findViewById(R.id.percent);
        SharedPreferences sh=getSharedPreferences("PercentData", MODE_PRIVATE);
        SharedPreferences.Editor editor=sh.edit();
        editor.putString("percent",precent.getText().toString()).apply();
    }


    public void firstThread(){
        new Thread(new Runnable() {
            public void run() {
                while (i < 70) {
                    i += 2;
                    progressHandler.post(new Runnable() {
                        public void run() {
                            progressbarOne.setProgress(i);
                        }
                    });
                    try {
                        Thread.sleep(90);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    public void secThread(){
        new Thread(new Runnable() {
            public void run() {
                while (i < 70) {
                    i += 2;
                    progressHandler.post(new Runnable() {
                        public void run() {
                            progressBarSec.setProgress(i);
                        }
                    });
                    try {
                        Thread.sleep(90);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
