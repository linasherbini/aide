package com.driver.aid.driver.repairShop.RepairStatus;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.driver.aid.Model.Order;

import java.util.ArrayList;
import java.util.List;

public class RepairStatusAdapter extends RecyclerView.Adapter<RepairStatusViewHolder> {

    List<Order> orderList = new ArrayList<>();
    private RepairStatusViewHolder.RepairStatusListener listener;

    public RepairStatusAdapter(RepairStatusViewHolder.RepairStatusListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RepairStatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RepairStatusViewHolder(viewGroup,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RepairStatusViewHolder repairStatusViewHolder, int i) {
        repairStatusViewHolder.bind(orderList.get(i));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public void setData(List<Order> orders) {
        this.orderList.clear();
        this.orderList.addAll(orders);
        notifyDataSetChanged();

    }
}
