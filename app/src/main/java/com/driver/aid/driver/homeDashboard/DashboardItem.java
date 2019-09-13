package com.driver.aid.driver.homeDashboard;

public class DashboardItem {
    private String title;
    private int icon;
    private DashboardType type;

    enum DashboardType{
        ROAD_SIGNS, REPAIR_SHOT,HINTS,ALERTS
    }

    public DashboardItem(String title, int icon, DashboardType type) {
        this.title = title;
        this.icon = icon;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public int getIcon() {
        return icon;
    }

    public DashboardType getType() {
        return type;
    }
}
