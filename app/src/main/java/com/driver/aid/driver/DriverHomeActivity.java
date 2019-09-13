package com.driver.aid.driver;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;

import com.driver.aid.LoggedInUserManager;
import com.driver.aid.R;
import com.driver.aid.RealmRemoteManager;
import com.driver.aid.SharedPreferenceManager;
import com.driver.aid.SplashActivity;
import com.driver.aid.alarts.AlertsActivity;
import com.driver.aid.driver.homeDashboard.HomeDashBoardFragment;
import com.driver.aid.profile.ProfileFragment;

public class DriverHomeActivity extends AppCompatActivity {


    private SharedPreferenceManager sharedPref;

    public static Intent newIntent(Context context) {
        return new Intent(context, DriverHomeActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home_screen);
        sharedPref = new SharedPreferenceManager(this);
        FragmentTabHost fragmentTabHost = findViewById(R.id.tabhost);
        fragmentTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        fragmentTabHost.addTab(getDashboard(fragmentTabHost), HomeDashBoardFragment.class, null);
        fragmentTabHost.addTab(getLiveChatTab(fragmentTabHost), DriverChatFragment.class, null);
        fragmentTabHost.addTab(getProfileTab(fragmentTabHost), ProfileFragment.class, null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        new MenuInflater(this).inflate(R.menu.home_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.logout){
            RealmRemoteManager.getInstance().logout();
            LoggedInUserManager.getInstance().logOut();
            AlertsActivity.cancelAlarms(sharedPref,this);
            startActivity(SplashActivity.newIntent(this));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private TabHost.TabSpec getDashboard(TabHost tabHost) {
        return tabHost.newTabSpec("Menu")
                .setIndicator("Menu");
    }

    private TabHost.TabSpec getLiveChatTab(TabHost tabHost) {
        return tabHost.newTabSpec("LiveChat")
                .setIndicator("Live Chat");
    }
    private TabHost.TabSpec getProfileTab(TabHost tabHost) {
        return tabHost.newTabSpec("Profile")
                .setIndicator("Profile");
    }


}
