package com.gadgetroid.collegemate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by gadgetroid on 29/05/15.
 */
public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String title= intent.getExtras().getString("title");
        String id = intent.getExtras().getString("id");
        Intent intent1 = new Intent(context, AlarmService.class);
        intent1.putExtra("title",title);
        intent1.putExtra("id",id);
        context.startService(intent1);

    }
}
