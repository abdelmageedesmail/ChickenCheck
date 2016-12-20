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
import android.support.v4.app.NotificationCompat;


public class LockReceiver extends BroadcastReceiver {

    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        mData(context);

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
}
