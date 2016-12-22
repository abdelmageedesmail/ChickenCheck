package yallapro.ibtdi.com.chickencheck;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;


public class GetUserRegisterServices extends Service {
    SharedPreferences sh;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
            sendBroadcast(new Intent(GetUserRegisterServices.this,LockReceiver.class));
        return super.onStartCommand(intent, flags, startId);
    }

  /*  private BroadcastReceiver mScreenStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Intent.ACTION_SCREEN_ON:
                    Toast.makeText(context, "Notification", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };*/



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
/*
    @Override
    protected void onHandleIntent(Intent intent) {

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        registerReceiver(mScreenStateReceiver, filter);

        sendBroadcast(new Intent(GetUserRegisterServices.this, LockReceiver.class));
}
 */
}
