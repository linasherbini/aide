package com.driver.aid.alarts;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.driver.aid.SharedPreferenceManager;

import java.io.IOException;

public class AlarmService extends Service {

    private MediaPlayer mp;
    private SharedPreferenceManager sharedPref;

    public AlarmService() {
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, AlarmService.class);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPref = new SharedPreferenceManager(getApplicationContext());
        String tone = sharedPref.getCurrentAlarmTone();
        mp = new MediaPlayer();
        try {
            AssetFileDescriptor afd = null;
            afd = getResources().getAssets().openFd(tone);
            mp = new MediaPlayer();
            mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            afd.close();
            mp.prepare();
            mp.setLooping(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mp != null) {
            mp.start();
        }
        if(!AlertsActivity.isAlarmScheduled(getApplicationContext(),sharedPref)){
            sharedPref.clearAlarms();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            mp.stop();
            mp.release();
        }

    }
}
