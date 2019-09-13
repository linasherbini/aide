package com.driver.aid.driver.homeDashboard;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class DashBoardAdapter extends RecyclerView.Adapter<DashboardViewHolder> {
    private final DashboardViewHolder.DashboardItemListener listener;
    List<DashboardItem> itemList = new ArrayList<>();

    public DashBoardAdapter(DashboardViewHolder.DashboardItemListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public DashboardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new DashboardViewHolder(viewGroup, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardViewHolder dashboardViewHolder, int i) {
        dashboardViewHolder.bind(itemList.get(i));
    }

    public void setItemList(List<DashboardItem> itemList) {
        this.itemList.clear();
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
