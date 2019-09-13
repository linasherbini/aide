package com.driver.aid.driver.repairShop.RepairStatus;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.driver.aid.Model.Order;
import com.driver.aid.R;

public class RepairStatusViewHolder extends RecyclerView.ViewHolder {
    TextView date;
    TextView status;

    public RepairStatusViewHolder(@NonNull ViewGroup viewGroup,RepairStatusListener listener) {
        super(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_holder_repair_status, viewGroup, false));
        date = itemView.findViewById(R.id.date);
        status = itemView.findViewById(R.id.status);
        itemView.setOnClickListener(v->{
            listener.onOrderClicked((Order) itemView.getTag());
        });
    }

    void bind(Order order) {
        date.setText(order.getDate());
        status.setText(order.getStatus());
        itemView.setTag(order);
    }

    public interface RepairStatusListener{
        public void onOrderClicked(Order order);
    }
}
