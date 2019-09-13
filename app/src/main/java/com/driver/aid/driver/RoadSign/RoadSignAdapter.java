package com.driver.aid.driver.RoadSign;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.driver.aid.Model.Content;

import java.util.ArrayList;
import java.util.List;

public class RoadSignAdapter extends RecyclerView.Adapter<RoadSignViewHolder> {

    List<Content> contentList=new ArrayList<>();
    RoadSignClickListner listner;
    public RoadSignAdapter(RoadSignClickListner listner) {
        this.listner=listner;
    }

    @NonNull
    @Override
    public RoadSignViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RoadSignViewHolder(viewGroup,listner);
    }

    @Override
    public void onBindViewHolder(@NonNull RoadSignViewHolder roadSignViewHolder, int i) {
            roadSignViewHolder.bind(contentList.get(i));
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    void setData(List<Content> contents){
        this.contentList.clear();
        this.contentList.addAll(contents);
        notifyDataSetChanged();
    }
    public interface RoadSignClickListner{
        void onRoadSignClicked(Content content);
    }
}
