package com.driver.aid;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SharedPreferenceManager {
    private static final String KEY_SPEED_ALARM = "KEY_SPEED_ALARM";
    SharedPreferences sharedPreferences;

    public SharedPreferenceManager(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static final String KEY_ALARM_TONE = "KEY_ALARM_TONE";
    String KEY_ALARM_IDS = "KEY_ALARM_IDS";

    public void updateAntiDrowseAlarmsIds(List<String> integerList) {
        sharedPreferences
                .edit()
                .putStringSet(KEY_ALARM_IDS, new HashSet<>(integerList)).apply();
    }

    public List<String> getSavedAntiDrowseId() {
        Set<String> ids = sharedPreferences.getStringSet(KEY_ALARM_IDS, null);
        List<String> idList = new ArrayList<>();
        if (ids != null) {
            idList.addAll(ids);
        }
        return idList;
    }

    public void updateSelectedTone(String assetTone) {
        sharedPreferences.edit().putString(KEY_ALARM_TONE, assetTone).apply();
    }

    public String getCurrentAlarmTone() {
        return sharedPreferences.getString(KEY_ALARM_TONE, "");
    }

    public void clearAlarms() {
        sharedPreferences.edit().remove(KEY_ALARM_IDS).apply();
    }

    public void setSpeedAlarmStatus(boolean isOn) {
        sharedPreferences.edit().putBoolean(KEY_SPEED_ALARM, isOn).apply();
    }

    public boolean getSpeedAlarmStatus() {
        return sharedPreferences.getBoolean(KEY_SPEED_ALARM, false);
    }
}
