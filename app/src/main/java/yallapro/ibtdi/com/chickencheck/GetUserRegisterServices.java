package yallapro.ibtdi.com.chickencheck;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;


public class GetUserRegisterServices extends IntentService {
    SharedPreferences sh;


    private BroadcastReceiver mScreenStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Intent.ACTION_SCREEN_ON:
                    Toast.makeText(context, "Notification", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    public GetUserRegisterServices() {
        super("GetUserRegisterServices");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        registerReceiver(mScreenStateReceiver, filter);

        sendBroadcast(new Intent(GetUserRegisterServices.this, LockReceiver.class));

    }
}
