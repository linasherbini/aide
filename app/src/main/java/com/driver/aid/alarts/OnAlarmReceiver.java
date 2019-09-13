package com.driver.aid.alarts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OnAlarmReceiver extends BroadcastReceiver {

    public static final String ACTION_ALARM_RECEIVER = "ACTION_ALARM_RECEIVER";

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(AlarmService.newIntent(context));
    }
}
