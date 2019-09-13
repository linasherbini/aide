package com.driver.aid.driver.repairShop;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.driver.aid.R;


public class RepairFragmentContainer extends Fragment implements TabLayout.OnTabSelectedListener {


    public static final String TAG = RepairFragmentContainer.class.getName();

    public RepairFragmentContainer() {
    }

    public static RepairFragmentContainer newInstance() {
        return new RepairFragmentContainer();
    }

    TabLayout tabLayout;
    ViewPager viewPager;
    RepairPageAdapter viewPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_repair_container, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        tabLayout.addTab(tabLayout.newTab().setText("Request Service"));
        tabLayout.addTab(tabLayout.newTab().setText("Request Status"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) view.findViewById(R.id.pager);

        viewPagerAdapter = new RepairPageAdapter(getFragmentManager());

        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(this);
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

        viewPager.setCurrentItem(tab.getPosition());
    }

}
