package com.driver.aid.alarts;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.driver.aid.R;
import com.driver.aid.SharedPreferenceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static android.content.Context.ALARM_SERVICE;

public class AntiDrowseDialog extends DialogFragment {

    public static final String TAG = AntiDrowseDialog.class.getName();
    private MediaPlayer mp;
    private SharedPreferenceManager sharedPreferenceManager;
    private AntiDrowseDialogListener listener;


    public static AntiDrowseDialog newInstance() {
        return new AntiDrowseDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        sharedPreferenceManager = new SharedPreferenceManager(getContext());
        View carView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_antidosness, null);

        AppCompatSpinner toneSpinner = carView.findViewById(R.id.alarms_tone_spinner);
        AppCompatSpinner durationSpinner = carView.findViewById(R.id.road_durationSpinner);
        AppCompatSpinner repetitionSpinner = carView.findViewById(R.id.repetitionSpinner);

        String[] durationArray = getResources().getStringArray(R.array.roadDuration);
        String[] repetitionArray = getResources().getStringArray(R.array.alarmRepetition);
        String[] toneArray = getResources().getStringArray(R.array.tones);


        toneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mp != null) {
                    if (mp.isPlaying()) {
                        mp.stop();
                        mp.release();
                        mp = new MediaPlayer();

                    }
                }
                String tone = toneArray[toneSpinner.getSelectedItemPosition()];


                try {
                    AssetFileDescriptor afd = null;
                    afd = getResources().getAssets().openFd(tone);
                    mp = new MediaPlayer();
                    mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    afd.close();
                    mp.prepare();
                    mp.setLooping(false);
                    mp.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(carView)
                .setPositiveButton("Save", (arg0, arg1) -> {

                    String selectedDuration = durationArray[durationSpinner.getSelectedItemPosition()];
                    String repetitionDuration = repetitionArray[repetitionSpinner.getSelectedItemPosition()];
                    String tone = toneArray[toneSpinner.getSelectedItemPosition()];
                    setupAlarm(selectedDuration, repetitionDuration, tone);
                    getDialog().dismiss();
                    mp.stop();
                });

        return builder.create();
    }

    private void setupAlarm(String duration, String repetition, String assetTone) {
        AlarmManager mgrAlarm = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
        int repetationInMinutes = Integer.parseInt(repetition);
        int durationInHours = Integer.parseInt(duration);
        int alarmCount = (durationInHours * 60) / repetationInMinutes;//60/5 = 12
        List<String> alarmIds = new ArrayList<>();
        for (int i = 0; i < alarmCount; ++i) {
            Intent intent = new Intent(getContext(), OnAlarmReceiver.class);
            intent.setAction(OnAlarmReceiver.ACTION_ALARM_RECEIVER);
            int randomId = Math.abs(new Random().nextInt());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), randomId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            int ALARM_TYPE = AlarmManager.ELAPSED_REALTIME_WAKEUP;
            long time = SystemClock.elapsedRealtime() + (TimeUnit.MINUTES.toMillis(repetationInMinutes) * (i + 1));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                mgrAlarm.setExactAndAllowWhileIdle(ALARM_TYPE, time, pendingIntent);
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                mgrAlarm.setExact(ALARM_TYPE, time, pendingIntent);
            else
                mgrAlarm.set(ALARM_TYPE, time, pendingIntent);

            alarmIds.add(String.valueOf(randomId));
        }
        sharedPreferenceManager.updateSelectedTone(assetTone);
        sharedPreferenceManager.updateAntiDrowseAlarmsIds(alarmIds);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener=(AntiDrowseDialogListener)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        listener.onDismissed();
    }

    interface AntiDrowseDialogListener {
        public void onDismissed();
    }
}
