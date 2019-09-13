package com.driver.aid.driver.homeDashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.driver.aid.alarts.AlertsActivity;
import com.driver.aid.R;
import com.driver.aid.driver.RoadSign.RoadSignListFragment;
import com.driver.aid.driver.repairShop.RepairFragmentContainer;
import com.driver.aid.driver.roadHints.HintListFragment;

import java.util.Arrays;
import java.util.List;

public class HomeDashBoardFragment extends Fragment implements DashboardViewHolder.DashboardItemListener {

    DashBoardAdapter adapter;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        List<DashboardItem> dashboardItemList = Arrays.asList(new DashboardItem("Repair shop", R.drawable.ic_repairshop, DashboardItem.DashboardType.REPAIR_SHOT),
                new DashboardItem("Road signs", R.drawable.ic_trafficlight, DashboardItem.DashboardType.ROAD_SIGNS)
                , new DashboardItem("Maintenance hints", R.drawable.ic_hints, DashboardItem.DashboardType.HINTS)
                , new DashboardItem("Alerts", R.drawable.ic_alerts, DashboardItem.DashboardType.ALERTS));
        adapter = new DashBoardAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setItemList(dashboardItemList);
    }

    @Override
    public void onItemClicked(DashboardItem.DashboardType dashboardType) {
        switch (dashboardType) {
            case ROAD_SIGNS:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, RoadSignListFragment.newInstance())
                        .addToBackStack(RoadSignListFragment.TAG)
                        .commit();
                break;
            case REPAIR_SHOT:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, RepairFragmentContainer.newInstance())
                        .addToBackStack(RepairFragmentContainer.TAG)
                        .commit();
                break;
            case HINTS:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, HintListFragment.newInstance())
                        .addToBackStack(HintListFragment.TAG)
                        .commit();
                break;
            case ALERTS:
                startActivity(new Intent(getContext(), AlertsActivity.class));
                break;
        }
    }
}
