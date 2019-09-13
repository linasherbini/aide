package com.driver.aid;

import android.app.Application;

import com.google.firebase.FirebaseApp;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class DriverAidApplication extends Application {

    public void onCreate() {
        super.onCreate();
        Realm.init(this);

    }
}
