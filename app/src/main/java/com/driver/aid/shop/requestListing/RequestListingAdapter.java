package com.driver.aid.shop.requestListing;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.driver.aid.Model.Order;
import com.driver.aid.R;

import java.util.ArrayList;
import java.util.List;

public class RequestListingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ItemType> orderList = new ArrayList<>();

    Context context;
    private CurrentRequestViewHolder.CurrentRequestListner currentRequestListener;
    private NewRequestViewHolder.NewRequestListener newRequestListener;

    public RequestListingAdapter(Context context, CurrentRequestViewHolder.CurrentRequestListner currentRequestListener, NewRequestViewHolder.NewRequestListener newRequestListener) {
        this.context = context;
        this.currentRequestListener = currentRequestListener;
        this.newRequestListener = newRequestListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        switch (type) {
            case 0: {
                return new RequestTitleViewHolder(viewGroup);
            }
            case 1: {
                return new CurrentRequestViewHolder(viewGroup, currentRequestListener);
            }
            case 2: {
                return new NewRequestViewHolder(viewGroup, newRequestListener);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof RequestTitleViewHolder) {
            ((RequestTitleViewHolder) viewHolder).bind(orderList.get(i).title);
        } else if (viewHolder instanceof CurrentRequestViewHolder) {
            ((CurrentRequestViewHolder) viewHolder).bind(orderList.get(i).order);
        } else if (viewHolder instanceof NewRequestViewHolder) {
            ((NewRequestViewHolder) viewHolder).bind(orderList.get(i).order);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return orderList.get(position).type.ordinal();
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public void setData(@NonNull List<Order> currentOrders, @NonNull List<Order> newRequests) {
        this.orderList.clear();
        if (!newRequests.isEmpty()) {
            orderList.add(new ItemType(context.getString(R.string.new_order_title)));
            for (Order order :
                    newRequests) {
                orderList.add(new ItemType(order, ItemType.ViewHolderType.NEW_REQUESTS));
            }
        }
        if (!currentOrders.isEmpty()) {
            orderList.add(new ItemType(context.getString(R.string.current_orders_title)));
            for (Order order :
                    currentOrders) {
                orderList.add(new ItemType(order, ItemType.ViewHolderType.CURRENT_REQUESTS));
            }
        }
        notifyDataSetChanged();
    }

    public static class ItemType {
        enum ViewHolderType {
            TITLE, CURRENT_REQUESTS, NEW_REQUESTS
        }

        public ItemType(Order order, ViewHolderType type) {
            this.order = order;
            this.type = type;
        }

        public ItemType(String title) {
            this.title = title;
            this.type = ViewHolderType.TITLE;
        }

        ViewHolderType type;
        Order order;
        String title;
    }
}

