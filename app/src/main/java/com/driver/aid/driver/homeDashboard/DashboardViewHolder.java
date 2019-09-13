package com.driver.aid.driver.homeDashboard;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.driver.aid.GlideApp;
import com.driver.aid.R;

public class DashboardViewHolder extends RecyclerView.ViewHolder {

    private final DashboardItemListener dashboardItemListener;
    ImageView icon;
    TextView title;

    public DashboardViewHolder(ViewGroup viewGroup, DashboardItemListener dashboardItemListener) {
        super(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_dashboard, viewGroup, false));
        icon = itemView.findViewById(R.id.icon);
        title = itemView.findViewById(R.id.title);
        this.dashboardItemListener = dashboardItemListener;
    }

    void bind(DashboardItem dashboardItem) {
        GlideApp.with(itemView)
                .load(dashboardItem.getIcon())
                .into(icon);
        icon.setColorFilter(itemView.getContext().getResources().getColor(R.color.colorPrimary));

        title.setText(dashboardItem.getTitle());
        itemView.setTag(dashboardItem.getType());
        itemView.setOnClickListener((v) -> dashboardItemListener.onItemClicked((DashboardItem.DashboardType) itemView.getTag()));
    }

   public interface DashboardItemListener {
        void onItemClicked(DashboardItem.DashboardType dashboardType);
    }
}
