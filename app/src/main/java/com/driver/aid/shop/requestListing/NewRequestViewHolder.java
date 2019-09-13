package com.driver.aid.shop.requestListing;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.driver.aid.Model.Order;
import com.driver.aid.R;

public class NewRequestViewHolder extends RecyclerView.ViewHolder {

    TextView driverName;
    TextView carType;
    TextView issue;
    TextView phoneNumber;
    TextView location;

    public NewRequestViewHolder(@NonNull ViewGroup viewGroup, NewRequestListener requestListener) {
        super(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_new_request, viewGroup, false));
        driverName = itemView.findViewById(R.id.driverName);
        carType = itemView.findViewById(R.id.carType);
        issue = itemView.findViewById(R.id.issue);
        phoneNumber = itemView.findViewById(R.id.phoneNumber);
        location = itemView.findViewById(R.id.location);
        itemView.setOnClickListener(c -> requestListener.onNewRqeustClicked((Order) itemView.getTag()));
        location.setOnClickListener(c -> {
            Order order = (Order) itemView.getTag();
            requestListener.onLocationClicked(order.getLat(), order.getLan());
        });
        phoneNumber.setOnClickListener(c -> {
            Order order = (Order) itemView.getTag();
            requestListener.onPhoneClicked(order.getDriverPhone());
        });
    }

    public void bind(Order order) {
        driverName.setText(itemView.getResources().getString(R.string.driver_name, order.getDriverName()));
        carType.setText(itemView.getResources().getString(R.string.cartype, order.getCarType()));
        issue.setText(itemView.getResources().getString(R.string.car_issue, order.getIssue()));
        phoneNumber.setText(itemView.getResources().getString(R.string.phone_number, order.getDriverPhone()));
        location.setText(R.string.location);
        itemView.setTag(order);

    }

    public interface NewRequestListener {
        public void onNewRqeustClicked(Order order);

        public void onLocationClicked(Double lat, Double lang);

        public void onPhoneClicked(String phoneNumber);
    }
}
