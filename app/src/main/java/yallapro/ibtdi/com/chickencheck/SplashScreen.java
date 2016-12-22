package yallapro.ibtdi.com.chickencheck;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    Runnable run;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        run = new Runnable() {

            @Override

            public void run() {

                startActivity(new Intent(SplashScreen.this,ChartActivity.class));
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
                finish();

            }
        };

        handler.postDelayed(run, 3000);
    }
}
