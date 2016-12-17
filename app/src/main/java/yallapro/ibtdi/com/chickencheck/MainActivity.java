package yallapro.ibtdi.com.chickencheck;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    ProgressBar progressbarOne,progressBarSec;
    TextView precent;
    Handler progressHandler = new Handler();
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressbarOne=(ProgressBar) findViewById(R.id.progressBarFloat);
        progressBarSec=(ProgressBar) findViewById(R.id.progressBarSecond);

        firstThread();

        secThread();

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
