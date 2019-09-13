package com.driver.aid.driver.repairShop;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.driver.aid.driver.repairShop.RepairStatus.RepairStatusFragment;

public class RepairPageAdapter extends FragmentStatePagerAdapter {


    public RepairPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return RequestRepairFragment.newInstance();
            case 1:
                return RepairStatusFragment.newInstance();

        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
