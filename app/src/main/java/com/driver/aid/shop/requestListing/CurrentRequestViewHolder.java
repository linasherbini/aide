package com.driver.aid.shop.requestListing;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.driver.aid.Model.Order;
import com.driver.aid.R;

public class CurrentRequestViewHolder extends RecyclerView.ViewHolder {

    TextView name;
    TextView typeAndIssue;
    TextView status;

    public CurrentRequestViewHolder(@NonNull ViewGroup viewGroup, CurrentRequestListner currentRequestListner) {
        super(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_current_request, viewGroup, false));
        name = itemView.findViewById(R.id.name);
        typeAndIssue = itemView.findViewById(R.id.carType);
        status = itemView.findViewById(R.id.status);
        itemView.setOnClickListener(c -> currentRequestListner.onCurrentRequestClicked((Order) itemView.getTag()));
    }

    public void bind(Order order) {
        name.setText(itemView.getResources().getString(R.string.driver_name, order.getDriverName()));
        typeAndIssue.setText(itemView.getResources().getString(R.string.car_type_and_issue, order.getCarType(), order.getServiceType()));
        status.setText(itemView.getResources().getString(R.string.status, order.getStatus()));
        itemView.setTag(order);
    }

    public interface CurrentRequestListner {
        public void onCurrentRequestClicked(Order order);
    }
}
