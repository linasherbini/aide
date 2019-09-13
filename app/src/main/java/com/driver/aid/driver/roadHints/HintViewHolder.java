package com.driver.aid.driver.roadHints;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.driver.aid.GlideApp;
import com.driver.aid.Model.Content;
import com.driver.aid.R;

public class HintViewHolder extends RecyclerView.ViewHolder {
    private final HintsAdapter.HintClickListener listener;
    ImageView icon;
    TextView title;
    public HintViewHolder(ViewGroup viewGroup, HintsAdapter.HintClickListener listner) {
        super(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_hint, viewGroup, false));
        icon = itemView.findViewById(R.id.icon);
        title = itemView.findViewById(R.id.title);
        this.listener = listner;
    }

    void bind(Content content) {
        GlideApp.with(icon)
                .load(content.getImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerInside()
                .into(icon);
        title.setText(content.getTitle());
        itemView.setTag(content);
        itemView.setOnClickListener((v) -> listener.onRoadSignClicked((Content) itemView.getTag()));
    }
}
