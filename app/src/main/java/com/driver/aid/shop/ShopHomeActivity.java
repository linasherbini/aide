package com.driver.aid.shop;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.driver.aid.LoggedInUserManager;
import com.driver.aid.Model.Order;
import com.driver.aid.R;
import com.driver.aid.RealmRemoteManager;
import com.driver.aid.SplashActivity;
import com.driver.aid.shop.requestDetails.RequestDetailsFragment;
import com.driver.aid.shop.requestListing.RequestListingFragment;

public class ShopHomeActivity extends AppCompatActivity implements RequestListingFragment.RequestListingInteractionListener{

    public static Intent newIntent(Context context) {
        return new Intent(context,ShopHomeActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rs_home_screen);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, RequestListingFragment.newInstance(),RequestListingFragment.TAG)
                .addToBackStack(RequestListingFragment.TAG)
                .commit();
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
            startActivity(SplashActivity.newIntent(this));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onOrderClicked(Order order) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, RequestDetailsFragment.newInstance(order),RequestDetailsFragment.TAG)
                .addToBackStack(RequestDetailsFragment.TAG)
                .commit();
    }
}
