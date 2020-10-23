package com.kardelenapp.dilencininuygulamasi;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import io.paperdb.Paper;

/**
 * Created by mustafa on 2/27/2018.
 */

public class AlarmReceiver extends BroadcastReceiver {
    Context context ;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        Log.v("TAG","ALARM");
        sendNotification();


        Date currentTime = Calendar.getInstance().getTime();
        Calendar rightNow = Calendar.getInstance();
        int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);

        if(currentHour< 12){
            Paper.init(context);

            Paper.book().write("gunluk_odul",0);

        }

    }


    public void sendNotification() {

            Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Bir tık, bir sadaka!")
                        .setContentText("Allah rızası için bi tık sadaka!")
                        .setContentIntent(pendingIntent);
                ;

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);



        mNotificationManager.notify(001, mBuilder.build());
    }
}
