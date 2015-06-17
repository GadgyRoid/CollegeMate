package com.gadgetroid.collegemate;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

/**
 * Created by gadgetroid on 29/05/15.
 */
public class AlarmService extends Service {
    public static NotificationManager mManager;
    public static int nId;

    @Override
    public IBinder onBind(Intent arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @SuppressWarnings("static-access")
    @Override
    public void onStart(Intent intent, int startId)
    {
        super.onStart(intent, startId);
        String title = intent.getExtras().getString("title");
        nId = Integer.valueOf(intent.getExtras().getString("id"));

        mManager = (NotificationManager) this.getApplicationContext().getSystemService
                (this.getApplicationContext().NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(this.getApplicationContext(),AssignmentDetails.class);
        intent1.putExtra("id",intent.getExtras().getString("id"));

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_assignment)
                .setContentTitle(title)
                .setContentText("One assignment due!");

        //Notification notification = new Notification
          //      (R.drawable.ic_assignment,"Assignment pending!", System.currentTimeMillis());
        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingNotificationIntent = PendingIntent.getActivity
                (this.getApplicationContext(),0, intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        //mBuilder.flags |= Notification.FLAG_AUTO_CANCEL;
        //notification.setLatestEventInfo(this.getApplicationContext(),
           //     "Assignment pending!", title, pendingNotificationIntent);

        //Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        mBuilder.setContentIntent(pendingNotificationIntent);
        mBuilder.setAutoCancel(true);
        mBuilder.setSound(alarmSound);

        mManager.notify(nId, mBuilder.build());
    }

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

}

