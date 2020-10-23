package com.kardelenapp.dilencininuygulamasi;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by mustafa on 2/27/2018.
 */

public class AutoStart extends BroadcastReceiver
{
    AlarmReceiver alarm = new AlarmReceiver();
    Context context;
    @Override
    public void onReceive(Context context, Intent intent)
    {
        this.context = context;
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            alarmPeriodSet();
        }
    }

    public void alarmPeriodSet(){
        Calendar calendar=Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY,10);

        AlarmManager manager= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent( context,AlarmReceiver.class);

        PendingIntent alarmintent=PendingIntent.getBroadcast(context,0,intent,0);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_HALF_DAY,alarmintent);
    }
}