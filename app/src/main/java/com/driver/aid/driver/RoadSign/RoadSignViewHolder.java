package com.driver.aid.driver.RoadSign;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.driver.aid.GlideApp;
import com.driver.aid.Model.Content;
import com.driver.aid.R;

public class RoadSignViewHolder extends RecyclerView.ViewHolder {
    private final RoadSignAdapter.RoadSignClickListner listener;
    ImageView roadSign;

    public RoadSignViewHolder(ViewGroup viewGroup, RoadSignAdapter.RoadSignClickListner listner) {
        super(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sign, viewGroup, false));
        roadSign = itemView.findViewById(R.id.sign);
        this.listener = listner;
    }

    void bind(Content content) {
        GlideApp.with(roadSign)
                .load(content.getImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerInside()
                .into(roadSign);
        itemView.setTag(content);
        itemView.setOnClickListener((v) -> listener.onRoadSignClicked((Content) itemView.getTag()));
    }
}
