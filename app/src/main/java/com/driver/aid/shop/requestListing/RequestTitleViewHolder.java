package com.driver.aid.shop.requestListing;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.driver.aid.R;

public class RequestTitleViewHolder extends RecyclerView.ViewHolder {

    TextView title;

    public RequestTitleViewHolder(@NonNull ViewGroup viewGroup) {
        super(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_request_title, viewGroup, false));
        title = itemView.findViewById(R.id.title);
    }

    public void bind(String title) {
        this.title.setText(title);
    }
}
