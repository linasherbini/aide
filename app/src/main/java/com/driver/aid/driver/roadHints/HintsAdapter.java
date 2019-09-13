package com.driver.aid.driver.roadHints;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.driver.aid.Model.Content;

import java.util.ArrayList;
import java.util.List;

public class HintsAdapter extends RecyclerView.Adapter<HintViewHolder> {

    List<Content> contentList=new ArrayList<>();
    HintClickListener listner;
    public HintsAdapter(HintClickListener listner) {
        this.listner=listner;
    }

    @NonNull
    @Override
    public HintViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new HintViewHolder(viewGroup,listner);
    }

    @Override
    public void onBindViewHolder(@NonNull HintViewHolder hintViewHolder, int i) {
            hintViewHolder.bind(contentList.get(i));
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
    public interface HintClickListener {
        void onRoadSignClicked(Content content);
    }
}
